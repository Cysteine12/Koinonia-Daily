from faster_whisper import WhisperModel

model = WhisperModel("base", device="cpu", compute_type="int8")

segments, info = model.transcribe("audio.m4a")

for seg in segments:
	print(seg.text)