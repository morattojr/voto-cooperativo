FROM eclipse-temurin:21-jdk-jammy
WORKDIR /app
COPY build/libs/voto-cooperativo-api-0.0.1-SNAPSHOT.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-Dspring.profiles.active=default", "-jar", "app.jar"]
