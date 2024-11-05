package com.example.demo.controller;

import com.example.demo.service.users.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user/")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/getinfo")
    public ResponseEntity<?> getInfo() {
        System.out.println("getinfo");
        userService.getInfo();
        return ResponseEntity.ok("send...");
    }
}
