package com.example.demo.controller;

import com.example.demo.models.HttpException;
import com.example.demo.service.users.UserServiceProducer;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/api/user/")
public class UserController {
    private final UserServiceProducer userService;
    @Autowired
    public UserController(UserServiceProducer userService) {
        this.userService = userService;
    }
    @GetMapping("/getinfo")
    public ResponseEntity<?> getInfo(@RequestHeader("Authorization") String authorizationHeader) {
        try {
            System.err.println("getInfo request");

            String id = UUID.randomUUID().toString();
            userService.getPendingRequests().put(id, new CompletableFuture<>());

            ResponseEntity<String> response = userService.getInfo(id, authorizationHeader);
            System.err.println("getInfo response: " + response);
            return response;
        } catch (JsonProcessingException e) {
            throw new HttpException(HttpStatus.INTERNAL_SERVER_ERROR, "Error converting to JSON");
        }
    }
}
