package org.example.service;

import lombok.RequiredArgsConstructor;
import org.example.models.request.SignInRequest;
import org.example.models.request.SignUpRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import java.net.http.HttpClient;
import java.net.URI;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;


@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private final Logger logger = LoggerFactory.getLogger(AuthenticationService.class);
    private final String tokenUri = "http://127.0.0.1:2020/realms/macygabr/protocol/openid-connect/token";
    private final String clientSecret = "6dj8rLdEqIoDErqAnolS1dXsTxEHIFOY";

    public ResponseEntity<String> signIn(SignInRequest signInData){
        try {
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(new URI(tokenUri))
                    .header("Content-Type", "application/x-www-form-urlencoded")
                    .POST(HttpRequest.BodyPublishers.ofString(
                            "client_id=ApiGateWay" +
                                    "&client_secret=" +clientSecret+
                                    "&grant_type=password" +
                                    "&username=" +signInData.getEmail()+
                                    "&password="+ signInData.getPassword()
                    ))
                    .build();
            logger.debug("Request send: {}", request.toString());
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            logger.debug("Response: {}", response.body());
            return ResponseEntity.ok(response.body());
        } catch (Exception e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    public ResponseEntity<String> signUp(SignUpRequest signUpData){
        logger.debug("signUpData: {}", signUpData);

        return ResponseEntity.ok("ok");
    }
}