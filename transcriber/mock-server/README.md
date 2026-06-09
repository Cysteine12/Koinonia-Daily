# Mock Server

A live mock server for handling the callbacks of transcription services, primarily used for testing purposes during development of the Koinonia Daily transcription workflow.

## Overview

This server provides a way to test the integration between the Koinonia Daily backend/frontend and the transcription worker (hosted on Modal). It uses **ngrok** to create a public URL that allows the remote worker to send transcription results back to your local machine via a webhook.

## Features

- **Trigger Transcription**: An endpoint to simulate the start of a transcription task.
- **Callback Handling**: A secure endpoint to receive transcription results.
- **Automated Tunneling**: Integrated ngrok support for local development.
- **Local Persistence**: Saves received callback data to `callback_data.json` for easy inspection.

## Prerequisites

- [Node.js](https://nodejs.org/) (v20+ recommended)
- [pnpm](https://pnpm.io/)
- [ngrok](https://ngrok.com/) account and auth token

## Getting Started

### 1. Installation

```bash
pnpm install
```

### 2. Configuration

Copy the `.env.sample` file to `.env` and fill in your credentials:

```bash
cp .env.sample .env
```

Required environment variables:
- `MODAL_API_KEY`: Your Modal API key.
- `MODAL_CALLBACK_SECRET`: A secret string used to verify incoming callbacks.
- `MODAL_WORKER_URL`: The URL of your Modal transcription worker.
- `NGROK_AUTH_TOKEN`: Your ngrok authentication token.

### 3. Running the Server

Using `pnpm`:
```bash
pnpm start
```

Or using `just`:
```bash
just start
```

The server will start on port `8000` and automatically establish an ngrok tunnel. The public URL will be printed in the console.

## API Endpoints

### `GET /`
Check if the server is running.

### `POST /api/v1/transcripts/trigger`
Triggers the transcription worker.
- **Body**: `{ "audioUrl": "...", "transcriptId": "..." }`
- **Action**: Generates a callback URL using the ngrok tunnel and calls the Modal worker.

### `POST /api/v1/transcripts/callback`
The webhook endpoint where the transcription worker sends results.
- **Headers**: `X-Callback-Secret` must match your `MODAL_CALLBACK_SECRET`.
- **Action**: Writes the received JSON body to `output/{filename}.json`.

## Testing

You can test the trigger endpoint using the provided `justfile` task:

```bash
just test
```

This sends a sample POST request to your local server.
o your local server.
