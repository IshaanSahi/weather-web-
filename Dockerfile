# Multi-stage Dockerfile for Spring Boot Application

# Stage 1: Build the application
FROM gradle:8.5-jdk21 AS build

WORKDIR /app

# Copy Gradle files from backend directory
COPY backend/build.gradle backend/settings.gradle backend/gradlew ./
COPY backend/gradle ./gradle

# Copy source code
COPY backend/src ./src
COPY Frontend/html/*.html ./src/main/resources/static/
COPY Frontend/css ./src/main/resources/static/css
COPY Frontend/javascript ./src/main/resources/static/javascript

# Build the application and run tests
RUN ./gradlew clean build --no-daemon

# Stage 2: Run the application
FROM eclipse-temurin:21-jre-alpine

WORKDIR /app

# Copy the built JAR from the build stage
COPY --from=build /app/build/libs/*.jar app.jar

# Expose the port (Render will use the PORT environment variable)
EXPOSE 8080

# Run the application with dynamic port binding
ENTRYPOINT ["java", "-jar", "app.jar"]
