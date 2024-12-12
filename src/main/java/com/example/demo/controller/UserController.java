package com.example.demo.controller;

import com.example.demo.service.HHService;
import com.example.demo.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
@Tag(name = "Профиль", description = "API для управления профилем пользователя")
public class UserController {
    private final HHService hhService;
    private final UserService userService;

    @Operation(summary = "Получение информации о пользователе")
    @GetMapping("/profile")
    public ResponseEntity<?> getUser() {
        Long userId = userService.getCurrentUser().getId();
        System.err.println("userId: " + userId);
        String response = hhService.profile(userId);
        return ResponseEntity.ok(response);
    }
}