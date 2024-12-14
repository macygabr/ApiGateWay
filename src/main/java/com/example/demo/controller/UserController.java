package com.example.demo.controller;

import com.example.demo.service.HHService;
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

    @Operation(summary = "Получение информации о пользователе")
    @GetMapping("/profile")
    public ResponseEntity<?> getUser() {
        String response = hhService.profile();
        return ResponseEntity.ok(response);
    }
}