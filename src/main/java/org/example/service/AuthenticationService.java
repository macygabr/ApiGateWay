package org.example.service;

import lombok.RequiredArgsConstructor;
import org.example.models.request.SignInRequest;
import org.example.models.request.SignUpRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import java.net.http.HttpResponse;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private final KeycloakService keycloakService;
    private final Logger logger = LoggerFactory.getLogger(AuthenticationService.class);

    public ResponseEntity<String> signIn(SignInRequest signInData) {
        return keycloakService.validateUser(signInData);
    }

    public  ResponseEntity<String> signUp(SignUpRequest signUpData) {
        return keycloakService.registerUser(signUpData.getUsername(), signUpData.getPassword(), signUpData.getEmail());
    }
}
