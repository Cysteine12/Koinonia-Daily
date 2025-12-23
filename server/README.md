Koinonia Daily – Server (Backend)

This folder contains the Spring Boot backend for the Koinonia Daily application. The server exposes REST APIs consumed by the React Native mobile application and handles authentication, business logic, persistence, and integrations.


---

🧱 Tech Stack

Java 17+

Spring Boot

Spring Web (REST APIs)

Spring Data JPA / Hibernate

Spring Security (JWT-based authentication)

PostgreSQL (production & local dev)

H2 (optional, for tests)

Lombok

Maven



---

📂 Project Structure

server/
├── src/
│   ├── main/
│   │   ├── java/com/koinonia/daily/
│   │   │   ├── config/        # Security, JWT, CORS, app configs
│   │   │   ├── controller/    # REST controllers
│   │   │   ├── service/       # Business logic
│   │   │   ├── repository/    # JPA repositories
│   │   │   ├── model/         # JPA entities
│   │   │   ├── dto/           # Request/response DTOs
│   │   │   ├── exception/     # Global exception handling
│   │   │   └── KoinoniaDailyApplication.java
│   │   └── resources/
│   │       ├── application.yml
│   │       └── db/migration/  # Flyway migrations (if enabled)
│   └── test/                  # Unit & integration tests
├── pom.xml
└── README.md


---

🚀 Getting Started

Prerequisites

Java 17 or newer

Maven 3.9+

PostgreSQL



---

🔧 Environment Configuration

Create a application-local.yml (or use environment variables):

spring:
  datasource:
    url: jdbc:postgresql://localhost:5432/koinonia_daily
    username: postgres
    password: postgres
  jpa:
    hibernate:
      ddl-auto: validate
    show-sql: false

jwt:
  secret: your-secret-key
  expiration: 86400000

> ⚠️ Never commit secrets. Use environment variables in production.




---

▶️ Run the Application

mvn spring-boot:run

Or:

mvn clean package
java -jar target/*.jar

The server will start on:

http://localhost:8080


---

🔐 Authentication & Security

JWT-based authentication

Stateless REST APIs

Role-based access control (RBAC)

Protected routes using Spring Security filters


Common roles:

USER

ADMIN



---

📡 API Conventions

Base path: /api/v1

JSON request/response format

Uses ResponseEntity<>

Pagination via Pageable


Example:

GET /api/v1/users?page=0&size=20


---

🧪 Testing

Run all tests:

mvn test

CI runs:

Unit tests

Integration tests with PostgreSQL service



---

🧰 CI/CD

GitHub Actions is used for:

Build & test on PRs (develop, main)

Static analysis (CodeQL)

Dependency scanning (Dependabot)



---

🛠️ Common Maven Commands

mvn clean
mvn test
mvn package
mvn spring-boot:run


---

🧠 Design Principles

Layered architecture (Controller → Service → Repository)

DTO-based API contracts

Explicit transaction boundaries (@Transactional)

Global exception handling

Minimal magic, explicit configuration



---

📌 Notes

This backend is optimized for low-to-moderate traffic

Designed for single-server deployment

Suitable for EC2, Fly.io, Railway, or Docker-based hosting



---

📄 License

This project is private and intended for internal use.


---

✝️ Project Vision

Koinonia Daily exists to deliver devotionals, sermons, and spiritual resources in a simple, reliable, and secure way.

> "And let us consider one another to provoke unto love and to good works." – Hebrews 10:24