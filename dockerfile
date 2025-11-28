# Use OpenJDK 17
FROM openjdk:17-jdk-slim

# Set working directory
WORKDIR /app

# Copy Maven wrapper and pom.xml first (for caching dependencies)
COPY .mvn/ .mvn
COPY mvnw pom.xml ./

# Copy source code
COPY src ./src

# Make Maven wrapper executable
RUN chmod +x mvnw

# Build the app (skip tests)
RUN ./mvnw clean package -DskipTests

# Expose port 8080
EXPOSE 8080

# Automatically find the jar in target and run it
CMD java -jar $(ls target/*.jar | head -n 1)
