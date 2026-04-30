# Koinonia Daily Transcriber

The **Koinonia Daily Transcriber** is a specialized Python-based service designed for high-efficiency audio-to-text transcription. It is primarily used to process spiritual teachings and sermons by Apostle Joshua Selman, providing the foundational data for the Koinonia Daily platform.

## 🎯 Purpose & Workflow

This service is intended to be run **locally** by administrators for mass transcription of audio messages. The workflow is as follows:

1.  **Audio Preparation:** Manually collect audio files (MP3/WAV) to be transcribed.
2.  **Transcription:** Use this service to generate text files from the audio.
3.  **Data Seeding:** The generated transcripts serve as the source material for seeding the application database.
4.  **Admin Integration:** Currently, transcripts are manually processed. In the future, an admin interface will allow for direct copy-pasting or uploading of these transcripts to create `Transcript` entities in the backend.

## 🚀 Tech Stack

-   **Core Engine:** [faster-whisper](https://github.com/SYSTRAN/faster-whisper) (A reimplementation of OpenAI's Whisper model using CTranslate2 for up to 4x speed increase).
-   **Model:** `base` (optimized for a balance between speed and accuracy).
-   **Hardware Optimization:** Configured for **CPU** execution with `int8` quantization to ensure compatibility without requiring a high-end GPU.
-   **Progress Tracking:** `tqdm` for real-time transcription progress.

## 🛠️ Prerequisites

Before running the transcriber, ensure you have the following installed:

1.  **Python 3.10+**
2.  **FFmpeg:** Required for audio processing.
    -   **macOS:** `brew install ffmpeg`
    -   **Ubuntu/Debian:** `sudo apt update && sudo apt install ffmpeg`
    -   **Windows:** Download from [ffmpeg.org](https://ffmpeg.org/download.html) and add to your PATH.

## 📥 Installation

The service includes a helper script to manage the environment and dependencies automatically.

1.  Navigate to the transcriber directory:
    ```bash
    cd transcriber
    ```
2.  Make the run script executable (Linux/macOS):
    ```bash
    chmod +x run.sh
    ```

## 📖 Usage

To transcribe an audio file, run the `run.sh` script followed by the path to your audio file.

```bash
./run.sh path/to/your/sermon.mp3
```

### What happens under the hood?
1.  **Environment Check:** Creates a Python virtual environment (`venv`) if it doesn't exist.
2.  **Dependency Management:** Automatically installs/updates requirements from `requirements.txt`.
3.  **Model Loading:** Loads the `base` Whisper model into memory.
4.  **Processing:** Transcribes the audio and provides a progress bar.
5.  **Output:** Generates a `.txt` file in the same directory with the same name as the audio file (e.g., `sermon.txt`).

## ⚙️ Configuration

The default configuration in `transcriber.py` is tuned for standard hardware:
-   **Model Size:** `base` (Change to `small`, `medium`, or `large-v3` in `transcriber.py` for higher accuracy at the cost of speed/memory).
-   **Device:** `cpu` (Change to `cuda` if you have an NVIDIA GPU and the appropriate drivers).
-   **Compute Type:** `int8` (Balanced precision for CPU).

## 📂 Project Structure

-   `run.sh`: The main entry point script for automated setup and execution.
-   `transcriber.py`: The core Python logic for transcription.
-   `requirements.txt`: Python library dependencies.
-   `transcribe-demo.py`: A lightweight debug script to test Whisper and FFmpeg connectivity.

## ⚠️ Troubleshooting

-   **FFmpeg not found:** Ensure `ffmpeg -version` works in your terminal.
-   **Memory Issues:** If the process crashes, ensure you have at least 2GB of free RAM for the `base` model.
-   **Slow Performance:** Transcription on CPU is slower than GPU. Ensure your laptop is plugged in for maximum performance.

---
Part of the Koinonia Daily ecosystem.
