# Multi-stage Dockerfile for building from the Backend/ directory
FROM eclipse-temurin:21-jdk AS builder

WORKDIR /app

COPY backend/backend/.mvn/ .mvn/
COPY backend/backend/mvnw backend/backend/pom.xml ./
RUN chmod +x mvnw

COPY backend/backend/src ./src
RUN ./mvnw clean package -DskipTests

FROM eclipse-temurin:21-jre

WORKDIR /app

COPY --from=builder /app/target/backend-0.0.1-SNAPSHOT.jar app.jar

EXPOSE 8080

CMD ["java", "-jar", "app.jar"]