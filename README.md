# Koinonia Daily

Koinonia Daily is a comprehensive digital ecosystem designed to centralize and provide seamless access to spiritual resources, teachings, sermons, and songs by Apostle Joshua Selman and the Koinonia Ministry.

The project is structured as a monorepo containing a high-performance backend, a modern mobile application, and a specialized audio transcription service.

## 🏗️ Project Structure

This monorepo is divided into three primary components:

### 1. [Server (Backend)](./server/README.md)
The central intelligence and API provider for the ecosystem.
- **Tech:** Java 25, Spring Boot 4.0.5, PostgreSQL, AWS S3/SES.
- **Features:** JWT Authentication, RBAC, Modular Layered Architecture, Rate Limiting.
- **Docs:** [API Documentation](./server/docs/api-docs.md)

### 2. [Mobile (Frontend)](./mobile/README.md)
A premium, custom-themed spiritual platform for end-users.
- **Tech:** React Native (Expo SDK 54), NativeWind v4, Zustand, React Query.
- **Features:** Audio/Video streaming, search, bookmarks, user history, and a custom gold-themed UI.

### 3. [Transcriber (Service)](./transcriber/README.md)
A local utility for generating transcripts to seed the application database.
- **Tech:** Python 3.10+, Faster-Whisper (OpenAI Whisper optimized).
- **Purpose:** Mass transcription of audio messages for search indexing and textual study.

## 🚀 Quick Start

To get the entire ecosystem running locally, follow the setup instructions in each directory:

1.  **Backend:** Setup PostgreSQL and run the Spring Boot app in `/server`.
2.  **Transcription:** (Optional) Use the Python service in `/transcriber` to process audio files.
3.  **Mobile:** Run the Expo development server in `/mobile`.

## 🛠️ Development Philosophy

- **High Performance:** Utilizing Java 25 and `faster-whisper` for backend and processing efficiency.
- **Premium UX:** Focusing on a polished, theme-driven mobile experience using NativeWind and Expo.
- **Data Integrity:** Standardized API responses and strict validation across all layers.

---
✝️ *And let us consider one another to provoke unto love and to good works.* — **Hebrews 10:24**
