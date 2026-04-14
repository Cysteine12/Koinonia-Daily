# Koinonia Daily – Server (Backend)

## Project Overview
Koinonia Daily is a Spring Boot-based backend for a Christian devotional application. It centralizes transcriptions, teachings, songs, and meetings of Koinonia by Apostle Joshua Selman, providing a searchable and accessible platform for spiritual resources.

### Core Technologies
- **Runtime:** Java 25 (OpenJDK)
- **Framework:** Spring Boot 3.5.10 (Spring Web, Spring Security, Spring Data JPA, Spring Mail)
- **Database:** PostgreSQL (with H2 for testing and Flyway for migrations)
- **Authentication:** JWT-based (jjwt), Stateless session management
- **Integrations:** 
    - **AWS S3:** For file storage and management.
    - **AWS SES / Mailtrap:** For email services.
    - **Sentry:** For error tracking and performance monitoring.
    - **Bucket4j & Caffeine:** For rate limiting and caching.
- **Code Quality:** Lombok, Checkstyle (Google Style), SpotBugs, JaCoCo (Coverage).

## Architecture & Design
The project follows a **Modular Layered Architecture**. Each feature is encapsulated within its own module under `org.eni.koinoniadaily.modules`.

### Package Structure
- `config/`: Application-wide configurations (Security, JWT, Rate Limiting, S3, Logging).
- `modules/`: Feature-specific modules (e.g., `auth`, `account`, `teaching`, `series`, `collection`).
    - Each module typically contains its own `Controller`, `Service`, `Repository`, `Entity`, and `DTO`s.
- `infrastructure/`: External service integrations (Email, Cloud Providers).
- `exceptions/`: Centralized exception handling via `GlobalExceptionHandler`.
- `utils/`: Common utilities and standard API response envelopes.
- `entity/`: Shared base entities (e.g., `BaseEntity`).

### Key Design Principles
- **Standardized Responses:** All API responses are wrapped in `ApiResponse` or `ErrorResponse` for consistency.
- **Base Entities:** Persistence models should extend `BaseEntity` to inherit standard auditing fields (`id`, `createdAt`, `updatedAt`).
- **RESTful Conventions:** Base path is `/api/v1`. Pagination is handled via Spring Data `Pageable`.
- **Validation:** Use Jakarta Validation constraints in DTOs; handled globally by `GlobalExceptionHandler`.

## Building and Running

### Prerequisites
- Java 25
- Maven 3.9+
- PostgreSQL (running instance)

### Key Commands
- **Run Application:** `./mvnw spring-boot:run`
- **Build Package:** `./mvnw clean package`
- **Run Tests:** `./mvnw test`
- **Checkstyle:** `./mvnw checkstyle:check`
- **SpotBugs:** `./mvnw spotbugs:check`

## Development Conventions

### Coding Style
- **Indentation:** 2 spaces (as per Google Java Style used in Checkstyle).
- **Lombok:** Use `@Getter`, `@Setter`, `@RequiredArgsConstructor`, and `@Slf4j` to minimize boilerplate.
- **Finality:** Prefer `private final` fields with constructor injection.

### Testing
- **Frameworks:** JUnit 5, Mockito, Spring Security Test.
- **Profiles:** Use `application-test.properties` for testing environments (defaults to H2/In-memory).
- **Coverage:** Aim for high coverage; JaCoCo reports are generated during the `test` phase.

### Security & Rate Limiting
- **Rate Limiting:** Managed via `RateLimitFilter` (Bucket4j). Configurations are in `RateLimitPolicy`.
- **Permissions:** Secured via `SecurityConfig` and `@PreAuthorize` annotations on controllers/services.
- **Secrets:** Never hardcode secrets. Use environment variables or `application-local.properties` (git-ignored).
