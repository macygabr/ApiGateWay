package com.example.demo.controller;


import com.example.demo.models.signIn.SignInRequest;
import com.example.demo.models.SignUpRequest;
import com.example.demo.service.authentication.AuthenticationServiceProducer;
import com.fasterxml.jackson.core.JsonProcessingException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/api/auth/")
@Tag(name = "Authentication API", description = "API для аутентификации и управления сессиями")
public class AuthController {
    private final AuthenticationServiceProducer authenticationService;

    @Autowired
    public AuthController(AuthenticationServiceProducer authenticationService) {
        this.authenticationService = authenticationService;
    }

    @Operation(summary = "Авторизация", description = "Вход пользователя с предоставлением учетных данных")
    @PostMapping("/signin")
    public ResponseEntity<?> signIn(@RequestBody SignInRequest request) {
        System.err.println("signIn request: " + request);
        String id = UUID.randomUUID().toString();
        authenticationService.getPendingRequests().put(id, new CompletableFuture<>());
        ResponseEntity<String> response = authenticationService.signIn(id, request);
        System.err.println("signIn response: " + response);
        return response;
    }

    @Operation(summary = "Регистрация", description = "Регистрация нового пользователя")
    @PostMapping("/signup")
    public ResponseEntity<?> signUp(@RequestBody SignUpRequest request) {
        System.err.println("signUp request: " + request);
        String response = authenticationService.signUp(request);
        System.err.println("signUp response: " + response);
        return ResponseEntity.ok(response);
    }

    @Operation(
            summary = "Выйти из системы",
            description = "Выполняет выход пользователя по токену авторизации",
            parameters = {
                    @Parameter(
                            name = "Authorization",
                            description = "Токен авторизации в формате 'Bearer <token>'",
                            required = true,
                            in = ParameterIn.HEADER
                    )
            }
    )
    @GetMapping("/logout")
    public ResponseEntity<?> logout(@RequestHeader("Authorization") String authorizationHeader) {
        System.err.println("logout");
        String id = UUID.randomUUID().toString();
        authenticationService.getPendingRequests().put(id, new CompletableFuture<>());
        ResponseEntity<String> response = authenticationService.logout(id,authorizationHeader);
        System.err.println("logout response: " + response);
        return response;
    }
}


