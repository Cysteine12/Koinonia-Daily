#!/bin/bash

# For first-time setup of this script
# run "chmod +x run.sh"

# Exit on error
set -e

VENV_DIR="venv"
SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"

cd "$SCRIPT_DIR"

# Create virtual environment if it doesn't exist
if [ ! -d "$VENV_DIR" ]; then
    echo "Creating virtual environment..."
    python -m venv "$VENV_DIR"

    echo "Activating virtual environment and installing dependencies..."
    
    if [[ "$OSTYPE" == "msys" || "$OSTYPE" == "win32" ]]; then
      source "$VENV_DIR/Scripts/activate"
    else 
      source "$VENV_DIR/bin/activate"
    fi
    pip install --upgrade pip
    pip install -r requirements.txt
else
    echo "Activating existing virtual environment..."
    if [[ "$OSTYPE" == "msys" || "$OSTYPE" == "win32" ]]; then
      source "$VENV_DIR/Scripts/activate"
    else 
      source "$VENV_DIR/bin/activate"
    fi
fi

# Check if audio file argument is provided
if [ "$#" -eq 0 ]; then
    echo "Error: No audio file provided."
    echo "Usage: ./run.sh <path_to_audio_file>"
    exit 1
fi

# Run the transcriber
echo "Starting transcription for file: $1"
python transcriber.py "$1"

# Deactivate the virtual environment
deactivate

echo "Transcription completed."