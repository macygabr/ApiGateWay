FROM openjdk:17-jdk-slim AS build

MAINTAINER Tigran Simonyan
LABEL version="1.0" description="Docker image for boomerang/client-side/deposit-service"

RUN apt-get update && apt-get install -y wget unzip
RUN wget https://services.gradle.org/distributions/gradle-8.13-milestone-3-all.zip -P /tmp \
    && unzip /tmp/gradle-8.13-milestone-3-all.zip -d /opt \
    && rm /tmp/gradle-8.13-milestone-3-all.zip
ENV PATH="/opt/gradle-8.13-milestone-3/bin:${PATH}"

WORKDIR /app

COPY . .
RUN gradle build
EXPOSE 8080

CMD ["java", "-jar", "app/app.jar"]