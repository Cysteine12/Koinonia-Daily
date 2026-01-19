# from faster_whisper import WhisperModel

# model = WhisperModel("base", device="cpu", compute_type="int8")

# segments, info = model.transcribe("audio.m4a")

# for seg in segments:
# 	print(seg.text)

import sys
import os
from pathlib import Path
from faster_whisper import WhisperModel
from tqdm import tqdm

def transcribe_audio(audio_path):
	"""Transcribe audio file and save to text file."""

	# Validate audio file exists
	if not os.path.isfile(audio_path):
		print(f"Audio file '{audio_path}' does not exist.")
		sys.exit(1)

	# Get audio file name without extension
	audio_file = Path(audio_path).stem
	output_file = f"{audio_file}.txt"
	print(f"Transcribing '{audio_path}'")
	print(f"Output file: '{output_file}'") 

	try:
		# Load Whisper model
		print("Loading Whisper model...")
		model = WhisperModel("base", device="cpu", compute_type="int8")

		# Transcribe audio file
		print("Starting transcription...")
		segments, info = model.transcribe(audio_path)

		# Convert generator to list to get total segments
		segments = list(segments)

		# Write transcription to text file with progress bar
		with open(output_file, "w", encoding="utf-8") as f:
			for seg in tqdm(segments, desc="Transcribing", unit="segment"):
				f.write(seg.text.strip() + "\n")

		print(f"\nTranscription completed successfully!'")
		print(f"Output saved to '{output_file}'")
		print(f"Total segments processed: {len(segments)}")

	except Exception as e:
		print(f"An error occurred during transcription: {e}")
		sys.exit(1)

if __name__ == "__main__":
	if len(sys.argv) != 2:
		print("Usage: python transcriber.py <audio_file>")
		sys.exit(1)

	audio_path = sys.argv[1]
	transcribe_audio(audio_path)