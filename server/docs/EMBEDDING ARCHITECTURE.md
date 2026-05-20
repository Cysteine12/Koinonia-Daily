# Embedding & Semantic Search Architecture

## 1. Overview
Koinonia Daily employs a state-of-the-art semantic search system to allow users to navigate spiritual teachings not just by keywords, but by conceptual meaning. This is achieved by transforming textual content into high-dimensional vector embeddings stored in a specialized vector database.

### Core Technologies
- **Runtime:** Java 25 (OpenJDK)
- **Framework:** Spring Boot 4.0.5 with Spring AI
- **Model:** OpenAI `text-embedding-3-small` (1536 dimensions)
- **Database:** PostgreSQL with `pgvector` extension
- **Infrastructure:** AWS S3 (source content), AWS SES (notifications)
- **Quality:** JaCoCo (Coverage), SpotBugs (Static Analysis)

---

## 2. The Content Lifecycle

The transformation from raw audio to searchable vector space follows a strict pipeline:

1.  **Transcription:** Raw audio is processed by the Python `transcriber` service to generate a base transcript.
2.  **Formatting:** Transcripts are refined into Markdown format, creating a `Teaching` entity.
3.  **Chunking:** The `Teaching` content is split into smaller, semantically coherent `TeachingChunk` entities.
4.  **Embedding Pipeline:** Each chunk is passed through the asynchronous embedding pipeline to generate its vector representation.
5.  **Vector Storage:** Embeddings are stored in the `chunk_embedding` table, indexed for fast similarity search.

---

## 3. Pipeline Architecture

### 3.1 Asynchronous Processing
To handle the high latency of LLM API calls and ensure system responsiveness, the embedding process is decoupled from the main request/response cycle.

-   **`EmbeddingJobDispatcher`:** Orchestrates the start of an embedding task.
-   **`LocalEmbeddingDispatcher`:** Utilizes Spring's `@Async` capabilities to execute jobs in background threads.
-   **`EmbeddingPipelineService`:** The core engine that manages the batching, API interaction, and state persistence.

### 3.2 State Management
We track the progress of every teaching and chunk through an explicit state machine:
-   `PENDING`: Content is ready but hasn't been processed.
-   `PROCESSING`: Currently being sent to the OpenAI API.
-   `EMBEDDED`: Vector successfully stored in PostgreSQL.
-   `FAILED`: An error occurred during processing.

---

## 4. Vector Search Implementation

### 4.1 pgvector Integration
We utilize the `vector` type in PostgreSQL to store the 1536-dimensional embeddings.
```sql
CREATE TABLE chunk_embedding (
    id UUID PRIMARY KEY,
    teaching_chunk_id UUID REFERENCES teaching_chunk(id),
    embedding vector(1536),
    model_version VARCHAR(255),
    created_at TIMESTAMP
);
```

### 4.2 Similarity Search
Search queries are converted into embeddings using the same OpenAI model and compared against stored vectors using **Cosine Distance** (`<=>`) or **Inner Product** (`<#>`).

---

## 5. Resilience & Error Handling

-   **Batch Processing:** Chunks are processed in configurable batches to optimize API throughput and reduce network overhead.
-   **Retry Logic:** Utilizing `Resilience4j` to handle transient network errors or rate limits from OpenAI.
-   **Timeout Management:** Chunks stuck in `PROCESSING` state are automatically reclaimed by the pipeline after a threshold period.
-   **Global Exception Handling:** Catastrophic failures are captured to mark the parent `Teaching` status as `FAILED`, ensuring system-wide visibility.

---

## 6. Build & Quality Standards

-   **JaCoCo:** Enforces high test coverage for the embedding logic and similarity search repositories.
-   **SpotBugs:** Scans for potential concurrency issues in the async dispatchers and memory leaks in vector processing.
-   **Checkstyle:** Ensures the code adheres to strict Google Java Style conventions.

---
✝️ *Expanding the reach of the Word through intelligent technology.*
