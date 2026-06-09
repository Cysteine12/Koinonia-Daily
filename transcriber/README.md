# Koinonia Daily Transcription System

The Koinonia Daily Transcription System is a multi-component solution designed to convert audio teachings and sermons into high-quality text data. It supports both local administrative workflows and cloud-based automated pipelines.

## 🏗️ Architecture Overview

The system is composed of three primary services, each serving a specific role in the transcription lifecycle:

### 1. [Local Transcription Service](./local-service)
**Role:** Admin-focused mass transcription.
- **Environment:** Runs locally on CPU (optimized with `int8` quantization).
- **Workflow:** Administrators place files in `inputs/` and receive text files in `outputs/`.
- **Use Case:** Initial bulk processing of legacy content and manual data seeding.
- **Tech:** `faster-whisper`, Python, `just`.

### 2. [Modal Cloud Service](./modal-service)
**Role:** Production-ready, automated transcription pipeline.
- **Environment:** Serverless GPU execution on [Modal](https://modal.com).
- **Workflow:** Receives a webhook trigger from the backend, downloads audio from Google Drive, transcribes using a T4 GPU, and posts results back via a secure callback.
- **Use Case:** Real-time processing of new sermons integrated directly into the Koinonia Daily backend.
- **Tech:** Modal, FastAPI, `faster-whisper`, NVIDIA CUDA.

### 3. [Mock Server](./mock-server)
**Role:** Development and Testing utility.
- **Environment:** Local Node.js server with ngrok tunneling.
- **Workflow:** Simulates the Spring Boot backend to test the Modal service webhooks locally.
- **Use Case:** End-to-end testing of the cloud pipeline without requiring the full Spring Boot infrastructure.
- **Tech:** Express.js, ngrok, pnpm.

---

## 🚀 Getting Started

To get started with a specific component, navigate to its directory and follow the instructions in its dedicated `README.md`:

- **Local Processing:** `cd local-service && just install`
- **Cloud Deployment:** `cd modal-service && just install`
- **Testing/Dev:** `cd mock-server && pnpm install`

## 🛠️ Shared Technology Stack

- **Model Engine:** [faster-whisper](https://github.com/SYSTRAN/faster-whisper) — A fast reimplementation of OpenAI's Whisper model.
- **Command Runner:** [Just](https://github.com/casey/just) is used across all services for consistent task management.
- **Language:** Python is used for the core transcription logic, while Node.js handles the mock testing environment.

---
Part of the [Koinonia Daily](https://github.com/Cysteine12/Koinonia-Daily) ecosystem.
