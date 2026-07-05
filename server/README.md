# Koinonia Daily Server

The **Koinonia Daily Server** is a robust, high-performance backend built with **Spring Boot 4.1.0** and **Java 25**. It serves as the central intelligence for the Koinonia Daily ecosystem, providing secure RESTful APIs for the mobile application and managing spiritual resources including teachings, transcripts, and multimedia assets.

## 🚀 Tech Stack

- **Language:** Java 25 (OpenJDK)
- **Framework:** Spring Boot 4.1.0
- **Security:** Spring Security with JWT (jjwt)
- **Database:** PostgreSQL with `pgvector` for Semantic Search
- **Migrations:** Flyway
- **AI/ML:** Spring AI (OpenAI Embeddings)
- **Storage & Messaging:** AWS S3 (File Storage), AWS SQS (Message Queuing), AWS SES (Email Services)
- **Observability:** Sentry (Error tracking), Spring Boot Actuator
- **Resilience:** Bucket4j (Rate Limiting), Resilience4j, Caffeine (Caching), Spring Retry (Transcription dispatch resilience)
- **Build & Quality:** Maven, Lombok, Checkstyle, SpotBugs, JaCoCo

## 📂 Architecture

The project follows a **Modular Layered Architecture** with specialized asynchronous pipelines for processing content and metadata.

### Project Structure
-   `config/`: Application-wide configurations (Security, JWT, S3, Rate Limiting, Async).
-   `modules/`: Feature-specific modules.
    -   `chunkembedding/`: Manages vector embeddings and semantic search logic.
    -   `search/`: Manages search autocomplete suggestions, result clicks, and search history.
    -   `teaching/`: Manages spiritual teachings and their lifecycle.
    -   `transcript/`: Manages transcripts and the asynchronous transcription workflow.
-   `infrastructure/`: External integrations (AWS, OpenAI, Embedding Dispatchers, Transcription Providers).
-   `exceptions/`: Global exception handling and custom error types.
-   `utils/`: Shared utilities and standard API response wrappers.

### 🔄 Asynchronous Transcription Pipeline
The server orchestrates audio transcription using a decoupled provider model:
1. **Trigger:** Admins trigger the workflow (`POST /api/v1/transcripts/workflow/trigger`).
2. **Dispatch:** The server delegates transcription tasks asynchronously to external providers (e.g. `ModalTranscriptionProvider` calling a faster-whisper GPU service).
3. **Resilience:** Built-in retry mechanism with exponential backoff handles network timeouts or provider server-side issues.
4. **Callback:** The external service sends transcription results back to `/api/v1/transcripts/workflow/callback` verified with a secret token, updating the database.

### 🔍 Search & Discovery
- **Autocomplete Suggestions:** Trigram-based fuzzy search using PostgreSQL `pg_trgm` extension.
- **Search Click History:** Recording user clicks on search results (`/api/v1/search/clicks`) to serve a personalized history feed (`/api/v1/search/recent`).

## 🛠️ Getting Started

### Prerequisites
-   **Java 25**
-   **Maven 3.9+**
-   **PostgreSQL** (Running instance)

### Installation & Run
1.  **Clone and navigate:**
    ```bash
    cd server
    ```
2.  **Environment Setup:** Create `src/main/resources/application-local.properties` (ignored by git):
    ```properties
    spring.datasource.url=jdbc:postgresql://localhost:5432/koinoniadaily
    spring.datasource.username=your_user
    spring.datasource.password=your_password
    jwt.secret=your_super_secret_key_at_least_32_characters
    ```
3.  **Run the application:**
    ```bash
    ./mvnw spring-boot:run
    ```
    The API will be available at `http://localhost:8080/api/v1`.

## 📚 Documentation

Detailed documentation of system components and APIs:

👉 **[API Documentation](docs/api-docs.md)**
👉 **[Transcription Architecture](docs/TRANSCRIPTION_ARCHITECTURE.md)**
👉 **[Embedding & Semantic Search Architecture](docs/EMBEDDING%20ARCHITECTURE.md)**

## 🛡️ Security & Conventions

-   **Authentication:** Stateless JWT-based authentication.
-   **Authorization:** Role-Based Access Control (RBAC) with `USER` and `ADMIN` roles.
-   **Standardized Responses:** All APIs return a consistent `ApiResponse<T>` or `ErrorResponse` envelope.
-   **Rate Limiting:** Protects sensitive endpoints (Login, Register) via Bucket4j filters.
-   **Validation:** Strict input validation using Jakarta Bean Validation (`@Valid`).

## 🧪 Quality & Testing

We maintain high code quality through automated checks:

```bash
# Run unit and integration tests
./mvnw test

# Check code style (Google Style)
./mvnw checkstyle:check

# Run static analysis
./mvnw spotbugs:check

# Generate coverage report (JaCoCo)
./mvnw verify
```

## 📄 License
This project is private and intended for internal use within the Koinonia Daily ecosystem.

---
✝️ *Building technology to advance the Kingdom.*
