# setup dockerfile for dev and build and runtime stages with temurin
FROM eclipse-temurin:25-jdk AS dev

# set working directory
WORKDIR /app

# copy source code
COPY . .

# install maven
RUN apt-get update && apt-get install -y maven && rm -rf /var/lib/apt/lists/*

COPY pom.xml .

RUN mvn dependency:go-offline

COPY . .

ENV SPRING_PROFILES_ACTIVE=development

EXPOSE 8080

CMD ["mvn", "spring-boot:run"]

# final stage for production
FROM eclipse-temurin:25-jre AS runtime

# set working directory
WORKDIR /app

# copy built application from build stage
COPY target/*.jar app.jar

# expose port
EXPOSE 8080

# run the application
ENTRYPOINT ["java", "-jar", "app.jar"]