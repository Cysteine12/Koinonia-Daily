#!/bin/bash

# For first-time setup of this script
# run "chmod +x setup.sh"

# Exit on error
set -e

VENV_DIR="venv"

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
