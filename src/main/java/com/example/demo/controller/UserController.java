package com.example.demo.controller;

import com.example.demo.service.users.UserServiceProducer;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
            String response = userService.getInfo(authorizationHeader);
            System.err.println("getInfo response: " + response);
            return ResponseEntity.ok(response);
        } catch (JsonProcessingException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error converting to JSON");
        }
    }
}
