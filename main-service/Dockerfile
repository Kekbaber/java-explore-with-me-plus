FROM maven:3.9.9-eclipse-temurin-21 AS build
WORKDIR /app
COPY . .
RUN mvn clean package -pl main-service -am -DskipTests
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/app/main-service/target/main-service.jar"]