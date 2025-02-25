plugins {
    java
    id("org.springframework.boot") version "3.3.4"
    id("io.spring.dependency-management") version "1.1.6"
}

group = "org.example"
version = "1.0"

repositories {
    mavenCentral()
}

dependencies {
    // Swagger (API Documentation)
    implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:2.2.0")

    // Keycloak (Authentication & Authorization)
    implementation("org.keycloak:keycloak-spring-boot-starter:21.0.0")

    // OAuth2 (Security & JWT)
    implementation("org.springframework.security:spring-security-oauth2-client")
    implementation("org.springframework.boot:spring-boot-starter-security")
    implementation("org.springframework.security:spring-security-oauth2-jose")
    implementation("org.springframework.security:spring-security-oauth2-resource-server")

    // Spring Boot Core Dependencies
    implementation("org.springframework.boot:spring-boot-starter-actuator")
    implementation("org.springframework.boot:spring-boot-starter-web")

    // Kafka (Messaging)
    implementation("org.springframework.kafka:spring-kafka")

    // Lombok (Boilerplate Reduction)
    compileOnly("org.projectlombok:lombok")
    annotationProcessor("org.projectlombok:lombok")

    // Testing Dependencies
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.springframework.kafka:spring-kafka-test")
    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
}


tasks.test {
    useJUnitPlatform()
}