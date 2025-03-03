package org.example.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.example.service.UserProfileService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
@Tag(name = "Профиль пользователя")
public class UserController {
    private final UserProfileService userProfileService;

    @Operation(summary = "Получение профиля пользователя")
    @GetMapping("/profile")
    public ResponseEntity<String> profile() {
        return userProfileService.getUserProfile();
    }
}