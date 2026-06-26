# Phase 1: Build the application using Maven
FROM maven:3.9.6-eclipse-temurin-17 AS build
WORKDIR /app
# Copy pom.xml and source code
COPY pom.xml .
COPY src ./src
# Package the application skipping unit tests for speed in CI/CD
RUN mvn clean package -DskipTests

# Phase 2: Create the lightweight runtime image
FROM eclipse-temurin:17-jre-jammy
WORKDIR /app
# Copy the compiled jar from the build phase
COPY --from=build /app/target/*.jar app.jar

# Run the background service
ENTRYPOINT ["java", "-jar", "app.jar"]