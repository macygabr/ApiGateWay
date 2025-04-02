package org.example.service;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import lombok.extern.slf4j.Slf4j;
import org.example.models.request.SignInRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class KeycloakService {

    @Value("${keycloak.token-endpoint-url}")
    private String tokenEndpointUrl;

    @Value("${keycloak.user-endpoint-url}")
    private String userEndpointUrl;

    @Value("${keycloak.client-secret}")
    private String clientSecret;

    private final String keycloakUrl = "http://127.0.0.1:2020";
    private final String realm = "macygabr";

    private final HttpClient client = HttpClient.newHttpClient();


    public ResponseEntity<String> registerUser(String username, String password, String email) {
        try {
            String registerUrl = String.format("%s/realms/%s/users", keycloakUrl, realm);

            String body = String.format("{\"username\":\"%s\",\"enabled\":true,\"email\":\"%s\",\"credentials\":[{\"type\":\"password\",\"value\":\"%s\"}]}",
                    username, email, password);

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(registerUrl))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(body))
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            return ResponseEntity.status(response.statusCode()).body(response.body());
        } catch (IOException | InterruptedException e) {
            log.error("Error registering user", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Registration failed");
        }
    }

    public ResponseEntity<String> validateUser(SignInRequest signInData) {
        try {
            String requestBody = String.format(
                    "client_id=ApiGateWay&client_secret=%s&grant_type=password&username=%s&password=%s",
                    clientSecret, signInData.getEmail(), signInData.getPassword());

            return sendPostRequest(tokenEndpointUrl, requestBody, "application/x-www-form-urlencoded");
        } catch (Exception e) {
            log.error("Error during sign-in", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Sign-in failed");
        }
    }

    private ResponseEntity<String> sendPostRequest(String url, String requestBody, String contentType) throws Exception {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(new URI(url))
                .header("Content-Type", contentType)
                .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                .build();

        log.debug("Sending request to: {}", url);

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        return ResponseEntity.status(response.statusCode()).body(response.body());
    }
}