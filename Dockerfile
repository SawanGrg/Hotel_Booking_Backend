# Stage 1: Build stage (Maven with JDK)
FROM  maven:3.8.4-openjdk-17-slim  AS hotel-booking-builder
WORKDIR /app
COPY pom.xml .
# This layer will be cached unless pom.xml changes
RUN mvn dependency:go-offline
COPY src ./src
RUN mvn package -DskipTests


# 2. Runtime stage
FROM eclipse-temurin:17-jre-jammy
WORKDIR /app
COPY --from=hotel-booking-builder /app/target/*.jar HotelBookingBackend.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "HotelBookingBackend.jar"]