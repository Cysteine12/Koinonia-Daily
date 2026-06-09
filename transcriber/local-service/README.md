# Koinonia Daily Transcriber

The **Koinonia Daily Transcriber** is a specialized Python-based service designed for high-efficiency audio-to-text transcription. It is primarily used to process spiritual teachings and sermons by Apostle Joshua Selman, providing the foundational data for the Koinonia Daily platform.

## 🎯 Purpose & Workflow

This service is part of the [Koinonia Daily Transcription System](../README.md) and is intended to be run **locally** by administrators for mass transcription of audio messages. While the [Modal Service](../modal-service) handles automated production requests, the local service is optimized for:

1.  **Bulk Audio Preparation:** Place existing sermon archives (MP3/WAV) into the `inputs/` directory.
2.  **Efficient Local Transcription:** Generate high-quality text files without cloud costs.
3.  **Data Seeding:** The generated transcripts in `outputs/` serve as the source material for seeding the application database.
4.  **Legacy Content Migration:** Ideal for processing thousands of past teachings that do not require real-time processing.

## 🚀 Tech Stack

-   **Core Engine:** [faster-whisper](https://github.com/SYSTRAN/faster-whisper) (A reimplementation of OpenAI's Whisper model using CTranslate2 for up to 4x speed increase).
-   **Model:** `base` (optimized for a balance between speed and accuracy).
-   **Hardware Optimization:** Configured for **CPU** execution with `int8` quantization to ensure compatibility across standard administrative hardware.
-   **Progress Tracking:** `tqdm` for real-time transcription progress bars in the CLI.

## 🛠️ Prerequisites

Before running the transcriber, ensure you have the following installed:

1.  **Python 3.10+**
2.  **FFmpeg:** Required for audio processing.
    -   **macOS:** `brew install ffmpeg`
    -   **Ubuntu/Debian:** `sudo apt update && sudo apt install ffmpeg`
    -   **Windows:** Download from [ffmpeg.org](https://ffmpeg.org/download.html) and add to your PATH.
3.  **Just:** Recommended command runner. [Installation Guide](https://github.com/casey/just).

## 📥 Installation

1.  Navigate to the local-service directory:
    ```bash
    cd local-service
    ```
2.  Initialize the environment and install dependencies:
    ```bash
    just install
    ```
    *If you don't have `just`, use the provided setup script:*
    ```bash
    ./setup.sh
    ```

## 📖 Usage

1.  Place your audio files in the `inputs/` folder.
2.  Run the transcription process:
    ```bash
    just transcribe
    ```
    *Manual alternative:*
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
