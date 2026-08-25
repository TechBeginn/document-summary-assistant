# Multi-stage build for optimal image size and minimal memory footprint
FROM eclipse-temurin:21-jdk AS builder

WORKDIR /app

# Copy Maven wrapper and POM first for layer caching
COPY .mvn/ .mvn/
COPY mvnw pom.xml ./
RUN chmod +x mvnw

# Copy source code and build the application JAR
COPY src ./src
RUN ./mvnw clean package -DskipTests

# Runtime stage using lightweight JRE (optimized for Render free tier 512MB RAM)
FROM eclipse-temurin:21-jre

WORKDIR /app

COPY --from=builder /app/target/backend-0.0.1-SNAPSHOT.jar app.jar

EXPOSE 8080

CMD ["java", "-jar", "app.jar"]
