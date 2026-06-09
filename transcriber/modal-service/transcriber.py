"""
Koinonia Daily — Modal Transcription Service
=============================================
Architecture:
  - FastAPI web endpoint receives POST /transcribe and returns 202 immediately
  - Spawns a background Modal function (transcribe_worker) for heavy lifting
  - Worker downloads audio from Google Drive, transcribes with faster-whisper,
    and POSTs the result back to Spring Boot via callbackUrl

Auth:
  - Inbound  (Spring Boot → Modal):   X-Api-Key header, validated against MODAL_API_KEY secret
  - Outbound (Modal → Spring Boot):   X-Callback-Secret header, value from MODAL_CALLBACK_SECRET secret

Secrets required in Modal dashboard:
  - "koinonia-daily-transcriber-secrets" containing:
      MODAL_API_KEY          — Spring Boot sends this when calling Modal
      MODAL_CALLBACK_SECRET  — Modal sends this when calling back Spring Boot

Volume:
  - "whisper-model-cache" — caches faster-whisper model weights across cold starts
"""

import os
import re
import time
import logging
from pathlib import Path

import modal

# ---------------------------------------------------------------------------
# Logging
# ---------------------------------------------------------------------------
logger = logging.getLogger(__name__)
logger.setLevel(logging.INFO)
handler = logging.StreamHandler()
handler.setFormatter(logging.Formatter("%(asctime)s - %(levelname)s - %(message)s"))
logger.addHandler(handler)

# ---------------------------------------------------------------------------
# Constants
# ---------------------------------------------------------------------------
MODEL_SIZE = "base"
MODEL_CACHE_DIR = "/root/.cache/huggingface"
MAX_AUDIO_BYTES = 120 * 1024 * 1024  # 120 MB

# ---------------------------------------------------------------------------
# Modal image — all packages baked in
# ---------------------------------------------------------------------------
image = (
    modal.Image.from_registry(
        "nvidia/cuda:12.3.2-cudnn9-runtime-ubuntu22.04",
        add_python="3.11"
    )
    .pip_install(
        "faster-whisper==1.1.1",
        "gdown==5.2.0",
        "fastapi[standard]",
        "requests==2.32.3",
    )
)

# ---------------------------------------------------------------------------
# Modal app + secrets + volume
# ---------------------------------------------------------------------------
app = modal.App("koinonia-daily-transcriber", image=image)

secrets = modal.Secret.from_name(
    "koinonia-daily-transcriber-secrets",
    required_keys=["MODAL_API_KEY", "MODAL_CALLBACK_SECRET"],
)

# Model weights are downloaded once and persisted here across container restarts
model_cache_vol = modal.Volume.from_name("whisper-model-cache", create_if_missing=True)

# ---------------------------------------------------------------------------
# Module-level model cache — populated lazily on first call per container
# ---------------------------------------------------------------------------
_whisper_model = None


def _get_model():
    """
    Returns the loaded WhisperModel, initializing it on first call per container.
    Subsequent calls on the same container return the cached instance immediately.
    """
    global _whisper_model

    if _whisper_model is None:
        from faster_whisper import WhisperModel

        logger.info(
            "Loading faster-whisper model '%s' (download_root=%s)",
            MODEL_SIZE,
            MODEL_CACHE_DIR,
        )
        _whisper_model = WhisperModel(
            MODEL_SIZE,
            device="cuda",
            compute_type="float16",
            download_root=MODEL_CACHE_DIR,
        )
        logger.info("Model loaded and ready.")

    return _whisper_model


# ---------------------------------------------------------------------------
# Background worker — runs on GPU
# ---------------------------------------------------------------------------
@app.function(
    gpu="T4",
    max_containers=5,         # allow up to 5 concurrent transcriptions
    volumes={MODEL_CACHE_DIR: model_cache_vol},
    secrets=[secrets],
    timeout=60 * 60,          # 60 min max per job (long sermons)
    scaledown_window=5 * 60,  # keep warm for 5 min between jobs
)
def transcribe_worker(transcript_id: int, audio_url: str, callback_url: str) -> None:
    """
    Full pipeline: download → validate → transcribe → callback.
    All errors are caught and reported back via callbackUrl.
    """
    logger.info(
        "Worker started — transcriptId=%s audioUrl=%s", transcript_id, audio_url
    )

    tmp_path: Path | None = None

    try:
        # --------------------------------------------------------------------
        # 1. Download audio from Google Drive
        # --------------------------------------------------------------------
        tmp_path = _download_audio(audio_url)

        # --------------------------------------------------------------------
        # 2. Validate file size
        # --------------------------------------------------------------------
        file_size = tmp_path.stat().st_size
        logger.info(
            "Downloaded '%s' — %.2f MB", tmp_path.name, file_size / (1024 * 1024)
        )

        if file_size > MAX_AUDIO_BYTES:
            raise ValueError(
                f"Audio file exceeds 120 MB limit ({file_size / (1024 * 1024):.1f} MB)"
            )

        # --------------------------------------------------------------------
        # 3. Transcribe
        # --------------------------------------------------------------------
        text, duration = _transcribe(tmp_path)

        # --------------------------------------------------------------------
        # 4. Callback — success
        # --------------------------------------------------------------------
        _send_callback(
            callback_url,
            {
                "transcriptId": transcript_id,
                "success": True,
                "duration": round(duration),
                "text": text,
                "filename": tmp_path.name,
            },
        )
        logger.info(
            "Transcription complete — transcriptId=%s duration=%.1fs chars=%d",
            transcript_id,
            duration,
            len(text),
        )

    except Exception as exc:
        logger.error(
            "Transcription failed — transcriptId=%s error=%s",
            transcript_id,
            exc,
            exc_info=True,
        )
        _send_callback(
            callback_url,
            {
                "transcriptId": transcript_id,
                "success": False,
                "error": str(exc),
            },
        )

    finally:
        # --------------------------------------------------------------------
        # 5. Cleanup — always remove the tmp file
        # --------------------------------------------------------------------
        if tmp_path and tmp_path.exists():
            try:
                tmp_path.unlink()
                logger.info("Cleaned up temp file: %s", tmp_path)
            except OSError as exc:
                logger.warning("Could not delete temp file %s: %s", tmp_path, exc)


# ---------------------------------------------------------------------------
# Private helpers (module-level functions
# ---------------------------------------------------------------------------

def _download_audio(audio_url: str) -> Path:
    """
    Downloads a Google Drive shareable link to /tmp.
    Uses fuzzy=True to handle the /file/d/<id>/view?usp=... format.
    gdown handles the large-file confirmation page automatically.
    Returns the Path to the downloaded file.
    """
    import gdown

    logger.info("Downloading audio from Google Drive — %s", audio_url)
    download_start = time.monotonic()

    try:
        downloaded_path = gdown.download(
            url=audio_url,
            output="/tmp/",
            fuzzy=True,    # handles /file/d/<id>/view?... share links
            quiet=False,   # logs progress to stdout (visible in Modal logs)
        )
    except Exception as exc:
        raise RuntimeError(f"Audio download failed: {exc}") from exc

    if downloaded_path is None:
        raise RuntimeError(
            "Audio download failed: gdown returned None. "
            "The file may be private or the link may have expired."
        )

    elapsed = time.monotonic() - download_start
    path = Path(downloaded_path)
    logger.info("Download complete in %.1fs — saved to %s", elapsed, path)
    return path


def _transcribe(audio_path: Path) -> tuple[str, float]:
    """
    Runs faster-whisper on the given file.
    Returns (full_text, audio_duration_seconds).
    """
    model = _get_model()

    logger.info("Starting transcription — file=%s", audio_path.name)
    transcribe_start = time.monotonic()

    segments, info = model.transcribe(
        str(audio_path),
        beam_size=5,
        language=None,                    # auto-detect language
        condition_on_previous_text=False, # faster, no multi-segment context needed
        vad_filter=True,                  # skip silent regions, speeds up long recordings
        word_timestamps=False,            # segment-level timestamps only
    )

    # segments is a generator — consume it to build the full transcript
    text_parts = []
    for segment in segments:
        logger.debug(
            "[%.2fs → %.2fs] %s", segment.start, segment.end, segment.text.strip()
        )
        text_parts.append(segment.text)

    full_text = " ".join(part.strip() for part in text_parts).strip()
    duration = info.duration
    elapsed = time.monotonic() - transcribe_start

    logger.info(
        "Transcription done in %.1fs — audio_duration=%.1fs language=%s",
        elapsed,
        duration,
        info.language,
    )
    return full_text, duration


def _send_callback(callback_url: str, payload: dict, max_retries: int = 3) -> None:
    import requests
    import random

    callback_secret = os.environ["MODAL_CALLBACK_SECRET"]

    for attempt in range(max_retries):
        try:
            response = requests.post(
                callback_url,
                json=payload,
                headers={
                    "Content-Type": "application/json",
                    "X-Callback-Secret": callback_secret,
                },
                timeout=30,
            )
            response.raise_for_status()
            logger.info(
                "Callback delivered to %s — HTTP %s", callback_url, response.status_code
            )
            return

        except requests.RequestException as exc:
            is_last_attempt = attempt == max_retries - 1

            if is_last_attempt:
                logger.exception(
                    "Callback delivery failed after %d attempts to %s",
                    max_retries,
                    callback_url,
                )
                return

            base_delay = 2**attempt  # 1, 2, 4 seconds
            jitter = random.uniform(0, base_delay)
            delay = base_delay + jitter

            logger.warning(
                "Callback attempt %d/%d failed — retrying in %.1fs. Error: %s",
                attempt + 1,
                max_retries,
                delay,
                exc,
            )
            time.sleep(delay)


# ---------------------------------------------------------------------------
# FastAPI web app — lightweight, no GPU needed
# ---------------------------------------------------------------------------
@app.function(
    secrets=[secrets],
    # No GPU — this function just validates the request and spawns the worker
)
@modal.asgi_app()
def web():
    """
    Thin FastAPI layer. Validates the inbound API key, spawns the GPU worker,
    and returns 202 immediately.
    """
    from fastapi import FastAPI, HTTPException, Request, status
    from fastapi.responses import JSONResponse
    from pydantic import BaseModel, HttpUrl

    web_app = FastAPI(title="Koinonia Transcriber")

    class TranscribeRequest(BaseModel):
        transcriptId: int
        audioUrl: HttpUrl
        callbackUrl: HttpUrl

    @web_app.post("/transcribe", status_code=status.HTTP_202_ACCEPTED)
    async def transcribe(request: Request, body: TranscribeRequest):

        # --------------------------------------------------------------------
        # Auth — validate inbound API key
        # --------------------------------------------------------------------
        api_key = request.headers.get("X-Api-Key")
        expected_key = os.environ["MODAL_API_KEY"]

        if not api_key or api_key != expected_key:
            logger.warning(
                "Rejected request for transcriptId=%s — invalid or missing X-Api-Key",
                body.transcriptId,
            )
            raise HTTPException(
                status_code=status.HTTP_401_UNAUTHORIZED,
                detail="Invalid or missing API key",
            )

        # --------------------------------------------------------------------
        # Spawn background worker — returns immediately
        # --------------------------------------------------------------------
        await transcribe_worker.spawn.aio(
            transcript_id=body.transcriptId,
            audio_url=str(body.audioUrl),
            callback_url=str(body.callbackUrl),
        )

        logger.info(
            "Job accepted and spawned — transcriptId=%s", body.transcriptId
        )

        return JSONResponse(
            status_code=status.HTTP_202_ACCEPTED,
            content={
                "success": True,
                "message": "Transcription job accepted and queued",
                "transcriptId": body.transcriptId,
            },
        )

    return web_app
