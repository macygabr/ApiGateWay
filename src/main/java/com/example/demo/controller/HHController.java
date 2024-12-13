package com.example.demo.controller;

import com.example.demo.models.filter.Filter;
import com.example.demo.service.HHService;
import com.example.demo.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;


@RestController
@RequiredArgsConstructor
@RequestMapping("/hh")
@Tag(name = "HH сервис")
public class HHController {
    private final HHService hhService;

    @Operation(summary = "Получение ссылки для регистрации в HH API")
    @GetMapping("/get-link")
    public ResponseEntity<?> getLink() {
        String response = hhService.getLink();
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @Operation(summary = "Регистрация пользователя в HH API")
    @PatchMapping("/registry")
    public ResponseEntity<?> registry(@RequestParam("code") String code) {
        String response = hhService.registry(code);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }


    @Operation(summary = "Старт процесса", description = "Запуск процесса в HH API")
    @PostMapping("/start")
    public ResponseEntity<?> start() {
        String response = hhService.start();
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @Operation(summary = "Остановка процесса", description = "Остановка процесса в HH API")
    @GetMapping("/stop")
    public ResponseEntity<?> stop() {
        String response = hhService.stop();
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @Operation(summary = "Установка фильтра", description = "Установка фильтра в HH API")
    @PostMapping("/filter")
    public ResponseEntity<?> setFilter(@RequestBody @Validated Filter filter) {
        System.out.println(filter);
        String response = hhService.filter(filter);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}