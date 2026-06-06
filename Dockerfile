# Phase 1: Build the application using Maven
FROM maven:3.8.5-openjdk-17 AS build
COPY . .
RUN mvn clean package -DskipTests

# Phase 2: Run the application using a lightweight Java runtime
FROM openjdk:17-jdk-slim
COPY --from=build /target/*.jar app.jar

# Expose the dynamic port Render gives us
EXPOSE 8080

# Run the Spring Boot JAR file
ENTRYPOINT ["java", "-jar", "app.jar"]