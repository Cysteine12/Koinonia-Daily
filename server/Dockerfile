# Build stage
FROM eclipse-temurin:25-jdk AS dev

WORKDIR /app

RUN apt-get update && apt-get install -y maven && rm -rf /var/lib/apt/lists/*

COPY pom.xml .

RUN mvn dependency:go-offline

COPY . .

RUN mvn clean package -DskipTests

# Development stage
FROM build as development

ENV SPRING_PROFILES_ACTIVE=development

EXPOSE 8080

CMD ["mvn", "spring-boot:run"]

# Runtime stage (production)
FROM eclipse-temurin:25-jre AS runtime

WORKDIR /app

COPY --from=build /app/target/*.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]