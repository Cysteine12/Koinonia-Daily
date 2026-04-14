# Koinonia Daily - Project Overview

Koinonia Daily is a comprehensive platform for spiritual teachings, featuring a monorepo structure with a Spring Boot backend, an Expo-based mobile application, and a Python-powered transcription service.

## Repository Structure

- `mobile/`: React Native (Expo SDK 54) mobile application.
- `server/`: Spring Boot 3.5.10 (Java 25) backend API.
- `transcriber/`: Python-based audio transcription service using `faster-whisper`.
- `.github/workflows/`: CI/CD pipelines for mobile and server components.

---

## 🏗️ Building and Running

### Mobile (`mobile/`)
Requires Node.js and npm/yarn.
```bash
cd mobile
npm install
npx expo start
```
*See `mobile/GEMINI.md` for detailed instructions and conventions.*

### Server (`server/`)
Requires Java 25 and Maven.
```bash
cd server
./mvnw spring-boot:run
```
*See `server/GEMINI.md` for detailed instructions and conventions.*

### Transcriber (`transcriber/`)
Requires Python 3.10+ and a virtual environment.
```bash
cd transcriber
./run.sh <path_to_audio_file>
```
*The script automatically manages the virtual environment and dependencies.*

---

## 🛠️ Development Conventions

### General
- **Monorepo Management:** Always ensure you are in the correct subdirectory before running component-specific commands.
- **CI/CD:** Follow the established GitHub Actions workflows for automated testing and deployment.
- **Environment Variables:** Use `.env` files (mobile) or `application-local.properties` (server) for local configuration. Never commit secrets.

### Mobile Development
- **Styling:** Use NativeWind (`className`) for layout and `useAppTheme` hook for theme-dependent colors.
- **State Management:** Use Zustand for client state and React Query for server data fetching.
- **Routing:** Expo Router (file-based).

### Server Development
- **Architecture:** Modular Layered Architecture (Controller → Service → Repository).
- **Responses:** Standardized `ApiResponse` or `ErrorResponse` envelopes.
- **Security:** JWT-based stateless authentication with role-based access control.

### Transcription
- **Model:** Uses `faster-whisper` (base model) for CPU-optimized transcription.
- **Output:** Generates a `.txt` file in the `transcriber/` directory with the transcribed content.

---

## ✝️ Project Vision
Koinonia Daily aims to provide a seamless and high-quality experience for users to access and interact with spiritual resources, sermons, and teachings.
