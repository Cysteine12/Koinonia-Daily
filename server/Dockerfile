# Development stage
FROM maven:3.9.11-eclipse-temurin-25 AS development

WORKDIR /app

COPY pom.xml .

RUN mvn dependency:go-offline

COPY . .

ENV SPRING_PROFILES_ACTIVE=dev

EXPOSE 8080

CMD ["mvn", "spring-boot:run"]

# Build stage
FROM maven:3.9.11-eclipse-temurin-25 AS build

WORKDIR /app

COPY pom.xml .

RUN mvn dependency:go-offline

COPY . .

RUN mvn clean package -DskipTests

# Runtime stage (production)
FROM eclipse-temurin:25-jre AS runtime

WORKDIR /app

RUN addgroup --system appgroup && adduser --system --ingroup appgroup appuser

COPY --from=build --chown=appuser:appgroup /app/target/*-SNAPSHOT.jar app.jar

USER appuser

ENV SPRING_PROFILES_ACTIVE=prod

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]
