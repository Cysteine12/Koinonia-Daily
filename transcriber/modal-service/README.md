# Koinonia Daily — Modal Transcription Service

This service provides a high-performance, GPU-accelerated audio transcription pipeline for Koinonia Daily. It is built using [Modal](https://modal.com/), [FastAPI](https://fastapi.tiangolo.com/), and [faster-whisper](https://github.com/SYSTRAN/faster-whisper).

## Architecture

The service operates in two phases to optimize resource usage:
1.  **Web Endpoint (CPU):** A lightweight FastAPI server that receives transcription requests. It validates the request and immediately spawns a background worker.
2.  **Worker (GPU):** A background process running on an NVIDIA T4 GPU. It downloads the audio from Google Drive, performs the transcription, and sends the result back to the Spring Boot backend via a callback.

## Project Structure

- `transcriber.py`: The core service logic, including the FastAPI web app and the Modal worker function.
- `justfile`: A command runner for common development tasks (environment setup, deployment, etc.).
- `requirements.txt`: Python dependencies for the local environment.

## Key Features

- **GPU Acceleration:** Uses NVIDIA T4 GPUs for fast transcription.
- **Model Caching:** Persists `faster-whisper` model weights in a Modal Volume (`whisper-model-cache`) to minimize cold-start times.
- **Google Drive Integration:** Automatically handles large file downloads from Google Drive shareable links.
- **Async Callbacks:** Returns a 202 Accepted status immediately and reports results/errors via a secure callback URL.
- **Robust Delivery:** Implements automatic retry logic with exponential backoff and jitter to ensure transcription results are successfully delivered even during brief backend outages.
- **VAD Filtering:** Uses Voice Activity Detection to skip silent regions, speeding up the processing of long sermons.

## Setup & Configuration

### Prerequisites
- Python 3.11+
- [Modal account](https://modal.com/) and CLI configured (`modal token set`)
- `just` command runner (optional but recommended)

### Secrets
The service requires a Modal Secret named `koinonia-daily-transcriber-secrets` containing:
- `MODAL_API_KEY`: A shared secret that the Spring Boot backend must include in the `X-Api-Key` header.
- `MODAL_CALLBACK_SECRET`: A secret that Modal sends back to Spring Boot in the `X-Callback-Secret` header to verify the callback authenticity.

## Development Commands

Use `just` to manage the project:

- **Setup Environment:** `just install` (creates venv and installs deps)
- **Run Locally (Dev Mode):** `just serve` (hot-reloads as you make changes)
- **Deploy to Modal:** `just deploy` (makes the service permanent on Modal)
- **Run Once:** `just transcribe`
- **Clean Up:** `just clean`

## API Usage

### POST `/transcribe`

**Headers:**
- `X-Api-Key`: `<MODAL_API_KEY>`

**Request Body:**
```json
{
  "transcriptId": 123,
  "audioUrl": "https://drive.google.com/file/d/...",
  "callbackUrl": "https://your-backend.com/api/v1/transcriptions/callback"
}
```

**Response:**
- `202 Accepted`: Transcription job queued.
- `401 Unauthorized`: Missing or invalid API key.
