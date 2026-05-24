# Koinonia Daily Server

The **Koinonia Daily Server** is a robust, high-performance backend built with **Spring Boot 4.0.5** and **Java 25**. It serves as the central intelligence for the Koinonia Daily ecosystem, providing secure RESTful APIs for the mobile application and managing spiritual resources including teachings, transcripts, and multimedia assets.

## 🚀 Tech Stack

- **Language:** Java 25 (OpenJDK)
- **Framework:** Spring Boot 4.0.5
- **Security:** Spring Security with JWT (jjwt)
- **Database:** PostgreSQL with `pgvector` for Semantic Search
- **Migrations:** Flyway
- **AI/ML:** Spring AI (OpenAI Embeddings)
- **Storage & Messaging:** AWS S3 (File Storage), AWS SQS (Message Queuing), AWS SES (Email Services)
- **Observability:** Sentry (Error tracking), Spring Boot Actuator
- **Resilience:** Bucket4j (Rate Limiting), Resilience4j, Caffeine (Caching)
- **Build & Quality:** Maven, Lombok, Checkstyle, SpotBugs, JaCoCo

## 📂 Architecture

The project follows a **Modular Layered Architecture** with a specialized **Asynchronous Embedding Pipeline** for processing spiritual content.

### Project Structure
-   `config/`: Application-wide configurations (Security, JWT, S3, Rate Limiting, Async).
-   `modules/`: Feature-specific modules.
    -   `chunkembedding/`: Manages vector embeddings and semantic search logic.
    -   `teaching/`: Manages spiritual teachings and their lifecycle.
-   `infrastructure/`: External integrations (AWS, OpenAI, Embedding Dispatchers).
-   `exceptions/`: Global exception handling and custom error types.
-   `utils/`: Shared utilities and standard API response wrappers.

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

## 📚 API Documentation

Detailed documentation of all available endpoints, request/response formats, and error codes can be found in:

👉 **[API Documentation](docs/api-docs.md)**

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
