import express from "express";
import ngrok from "@ngrok/ngrok";
import fs from "fs";
import path from "path";

const app = express();

const PORT = 8000;
const OUTPUT_DIR = "output";
let URL = null;
let listener = null;

app.use(express.json());

app.get("/", (req, res) => {
    res.json({ message: "Mock server is running" });
});

app.post("/api/v1/transcripts/workflow/trigger", async (req, res) => {
    try {
        console.log("Received trigger for transcription");

        if (!req.body.audioUrl) {
            return res.status(400).json({ success: false, message: "Missing audioUrl" });
        }

        const data = {
            transcriptId: req.body.transcriptId || Math.floor(Math.random() * 1000000),
            audioUrl: req.body.audioUrl,
            callbackUrl: `${URL}/api/v1/transcripts/workflow/callback`
        }

        console.log("Triggering worker with data:", data);

        const response = await fetch(process.env.MODAL_WORKER_URL + "/transcribe", {
            method: "POST",
            headers: {
                "Content-Type": "application/json",
                "X-API-Key": process.env.MODAL_API_KEY
            },
            body: JSON.stringify(data)
        });

        if (!response.ok) {
            const errorText = await response.text();
            throw new Error(`Worker failed: ${response.status} ${errorText}`);
        }

        const responseData = await response.json();
        console.log("Response from Modal worker:", responseData);
    
        res.status(200).json({ success: true, message: "Trigger received", transcriptId: data.transcriptId });
    } catch (err) {
        console.error("Error occurred while processing trigger:", err);
        res.status(500).json({ success: false, message: "Internal server error", error: err.message });
    }
});

app.post("/api/v1/transcripts/workflow/callback", (req, res) => {
    console.log("Received callback:");

    if (req.headers["content-type"] !== "application/json" || 
        req.headers["x-callback-secret"] !== process.env.MODAL_CALLBACK_SECRET
    ) {
        console.warn("Invalid callback received — missing or incorrect secret");
        return res.status(400).json({ success: false, message: "Invalid callback" });
    }

    const filename = req.body.filename || `callback_${Date.now()}`;
    const filePath = path.join(OUTPUT_DIR, `${filename}.json`);

    fs.writeFileSync(filePath, JSON.stringify(req.body, null, 2));

    req.body.text = `Response text has been saved to output/${filename}.json for inspection`;
    console.log(req.body);

    res.status(200).json({ success: true, message: "Callback received" });
});

const server = app.listen(PORT, async () => {
    console.log(`Mock server listening on port ${PORT}`);
    
    try {
        listener = await ngrok.forward({
            addr: PORT,
            authtoken: process.env.NGROK_AUTH_TOKEN
        });
        URL = listener.url();
        console.log(`Mock server running at ${URL}`);
    } catch (err) {
        console.error("Failed to establish ngrok tunnel:", err);
        process.exit(1);
    }
});
    
const shutdown = async (signal) => {
    console.log(`Received ${signal}. Shutting down gracefully...`);

    server.close(async () => {
        try {
            if (listener) {
                await listener.close();
                console.log("Ngrok tunnel closed");
            }
        } catch (err) {
            console.error("Error closing ngrok tunnel:", err);
        } finally {
            console.log("HTTP server closed");
            process.exit(0);
        }
    });
}

process.on("SIGINT", () => shutdown("SIGINT"));
process.on("SIGTERM", () => shutdown("SIGTERM"));