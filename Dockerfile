FROM eclipse-temurin:21-jdk-alpine AS build

RUN apk add --no-cache maven
WORKDIR /app

COPY pom.xml .
COPY src ./src

RUN mvn clean package
EXPOSE 8080
CMD ["java", "-jar", "/app/target/ApiGateWay-0.0.1.jar"]
