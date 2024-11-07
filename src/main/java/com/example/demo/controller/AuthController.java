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
    public ResponseEntity<?> signIn(@RequestBody SignInRequest request) throws JsonProcessingException {
        System.err.println("signIn request: " + request);
        ResponseEntity<String> response = authenticationService.signIn(request);
        System.err.println("signIn response: " + response);
        return response;
    }

    @PostMapping("/signup")
    public ResponseEntity<?> signUp(@RequestBody SignUpRequest request) throws JsonProcessingException {
        System.err.println("signUp request: " + request);
        String response = authenticationService.signUp(request);
        System.err.println("signUp response: " + response);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/logout")
    public ResponseEntity<?> logout() {
        System.err.println("logout");
        authenticationService.logout();
        return ResponseEntity.ok("send...");
    }
}


