import whisper
import subprocess
import sys
import os

AUDIO_FILE = "sample.mp3"   # change to your file

print("==== WHISPER DEBUG SCRIPT ====")

# 1. Check file exists
if not os.path.exists(AUDIO_FILE):
    print(f"ERROR: File not found -> {AUDIO_FILE}")
    sys.exit(1)

print(f"[OK] File exists: {AUDIO_FILE}")

# 2. Test ffmpeg manually
print("\n[TEST] Running ffmpeg to decode audio...")
try:
    result = subprocess.run(
        ["ffmpeg", "-i", AUDIO_FILE, "-f", "wav", "-"],
        stdout=subprocess.PIPE,
        stderr=subprocess.PIPE
    )
    stderr_output = result.stderr.decode("utf-8", errors="ignore")
    stdout_output = result.stdout
    print("[OK] ffmpeg responded.")
except Exception as e:
    print(f"[ERROR] ffmpeg failed: {e}")
    sys.exit(1)

# If ffmpeg printed errors in stderr that indicate a decode issue
if "Error" in stderr_output or "Invalid" in stderr_output:
    print("\n[ERROR] FFmpeg cannot decode this audio file!")
    print(stderr_output)
    sys.exit(1)

print("[OK] ffmpeg can decode the audio.\n")

# 3. Load model
print("Loading Whisper model... (this takes some seconds on CPU)")
try:
    model = whisper.load_model("tiny")  # small enough for CPU
    print("[OK] Model loaded.\n")
except Exception as e:
    print(f"[ERROR] Failed to load Whisper: {e}")
    sys.exit(1)

# 4. Transcribe
print("Transcribing now...\n")
try:
    result = model.transcribe(AUDIO_FILE, fp16=False)
    print("\n==== TRANSCRIPTION OUTPUT ====")
    print(result["text"])
    print("\n==== DONE ====")
except Exception as e:
    print(f"[ERROR] Transcription failed: {e}")
    sys.exit(1)