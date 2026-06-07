# Koinonia Daily Transcriber

The **Koinonia Daily Transcriber** is a specialized Python-based service designed for high-efficiency audio-to-text transcription. It is primarily used to process spiritual teachings and sermons by Apostle Joshua Selman, providing the foundational data for the Koinonia Daily platform.

## 🎯 Purpose & Workflow

This service is intended to be run **locally** by administrators for mass transcription of audio messages. The workflow is as follows:

1.  **Audio Preparation:** Place audio files (MP3/WAV) into the `inputs/` directory.
2.  **Transcription:** Use this service to generate text files from the audio.
3.  **Data Seeding:** The generated transcripts in the `outputs/` directory serve as the source material for seeding the application database.
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
3.  **Just (Optional):** A handy command runner. [Installation Guide](https://github.com/casey/just).

## 📥 Installation

The service includes a helper script to manage the environment and dependencies automatically.

1.  Navigate to the transcriber directory:
    ```bash
    cd transcriber/local-service
    ```
2.  Initialize the environment and install dependencies:
    ```bash
    ./setup.sh
    ```
    *Or, if you have `just` installed:*
    ```bash
    just install
    ```

## 📖 Usage

1.  Place your audio files (e.g., `.mp3`, `.wav`) in the `inputs/` folder.
2.  Run the transcription process:
    ```bash
    just transcribe
    ```
    *If not using `just`, activate the virtual environment and run the script manually:*
    ```bash
    source venv/bin/activate
    python transcriber.py
    ```

### What happens under the hood?
1.  **Environment Check:** Uses the Python virtual environment (`venv`).
2.  **Model Loading:** Loads the `base` Whisper model into memory.
3.  **Processing:** Scans the `inputs/` directory, transcribes each file, and provides a progress bar.
4.  **Output:** Generates a `.txt` file in the `outputs/` directory for each processed audio file.

## ⚙️ Configuration

The default configuration in `transcriber.py` is tuned for standard hardware:
-   **Model Size:** `base` (Change to `small`, `medium`, or `large-v3` in `transcriber.py` for higher accuracy at the cost of speed/memory).
-   **Device:** `cpu` (Change to `cuda` if you have an NVIDIA GPU and the appropriate drivers).
-   **Compute Type:** `int8` (Balanced precision for CPU).

## 📂 Project Structure

-   `justfile`: Command runner configuration for common tasks.
-   `setup.sh`: Script for automated virtual environment setup.
-   `transcriber.py`: The core Python logic for transcription.
-   `inputs/`: Place audio files here.
-   `outputs/`: Transcribed text files are saved here.
-   `requirements.txt`: Python library dependencies.

## ⚠️ Troubleshooting

-   **FFmpeg not found:** Ensure `ffmpeg -version` works in your terminal.
-   **Memory Issues:** If the process crashes, ensure you have at least 2GB of free RAM for the `base` model.
-   **Slow Performance:** Transcription on CPU is slower than GPU. Ensure your laptop is plugged in for maximum performance.

---
Part of the Koinonia Daily ecosystem.
