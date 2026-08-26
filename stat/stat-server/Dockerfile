FROM maven:3.9.9-eclipse-temurin-21 AS build
WORKDIR /app
COPY . .
RUN mvn clean package -pl stat/stat-server -am -DskipTests
EXPOSE 9090
ENTRYPOINT ["java", "-jar", "/app/stat/stat-server/target/stat-server.jar"]