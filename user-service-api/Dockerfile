FROM eclipse-temurin:21-jdk-jammy
WORKDIR /app
COPY build/libs/user-service-api-0.0.1-SNAPSHOT.jar app.jar
EXPOSE 8081
ENTRYPOINT ["java", "-Dspring.profiles.active=default", "-jar", "app.jar"]
