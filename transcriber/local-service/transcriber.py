import sys
from pathlib import Path

from faster_whisper import WhisperModel
from tqdm import tqdm

INPUT_DIR = Path("inputs")
OUTPUT_DIR = Path("outputs")

MODEL_SIZE = "base"

def transcribe_audio(model: WhisperModel, audio_path: Path):

    output_path = OUTPUT_DIR / f"{audio_path.stem}.txt"

    print(f"\nTranscribing: {audio_path.name}")

    segments, info = model.transcribe(str(audio_path))

    with open(output_path, "w", encoding="utf-8") as f:

        progress = tqdm(
            total=info.duration,
            desc=audio_path.name,
            unit="sec",
        )

        for seg in segments:
            f.write(seg.text.strip() + "\n")
            progress.update(seg.end - seg.start)

        progress.close()

    print(f"Saved: {output_path.name}")


def main():

    audio_extensions = {'.mp3', '.wav', '.m4a', '.flac', '.ogg', '.opus', '.webm'}
    audio_files = [f for f in INPUT_DIR.glob("*") if f.suffix.lower() in audio_extensions]


    if not audio_files:
        print("No audio files found.")
        return
    
    print(f"Found {len(audio_files)} audio files. Loading model...")

    model = WhisperModel(
        MODEL_SIZE,
        device="cpu",
        compute_type="int8"
    )

    for audio_file in audio_files:
        try:
            transcribe_audio(model, audio_file)
        except (RuntimeError, OSError, ValueError) as e:
            print(f"Failed: {audio_file.name}: {e}", file=sys.stderr)
        except KeyboardInterrupt:
            print("\nTranscription interrupted by user.")
            sys.exit(1)
        except Exception as e:
            print(f"Failed: {audio_file.name}: {e}")


if __name__ == "__main__":
    main()