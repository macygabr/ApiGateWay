package org.example.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
@Tag(name = "Профиль пользователя")
public class UserProfileController {

    private final UserProfileController userProfileController;

    @Operation(summary = "Регистрация пользователя")
    @PostMapping("/profile")
    public ResponseEntity<String> profile() {
        return userProfileController.profile();
    }
}