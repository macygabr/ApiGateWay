package com.example.demo.controller;


import com.example.demo.models.SignInRequest;
import com.example.demo.models.SignUpRequest;
import com.example.demo.service.authentication.AuthenticationServiceProducer;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private final AuthenticationServiceProducer authenticationService;

    @Autowired
    public AuthController(AuthenticationServiceProducer authenticationService) {
        this.authenticationService = authenticationService;
    }

    @PostMapping("/signin")
    public ResponseEntity<?> signIn(@RequestBody SignInRequest request) {
        try {
            System.out.println("signIn: " + request);
            String response = authenticationService.signIn(request);
            System.out.println("response: " + response);
            return ResponseEntity.ok(response);
        } catch (JsonProcessingException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error converting to JSON");
        }
    }

    @PostMapping("/signup")
    public ResponseEntity<?> signUp(@RequestBody SignUpRequest request) {
        try {
            System.out.println("signUp: " + request);
            authenticationService.signUp(request);
            return ResponseEntity.ok("send...");
        } catch (JsonProcessingException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error converting to JSON");
        }
    }

    @GetMapping("/logout")
    public ResponseEntity<?> logout() {
        System.out.println("logout");
        authenticationService.logout();
        return ResponseEntity.ok("send...");
    }
}


