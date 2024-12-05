package com.example.demo.controller;

import com.example.demo.service.hhrecruter.HHServiceProducer;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.headers.Header;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/api/hh/")
@Tag(name = "HH API", description = "API для работы с HH сервисами")
public class HHController {
    private final HHServiceProducer hhServiceProducer;

    @Autowired
    public HHController(HHServiceProducer hhServiceProducer) {
        this.hhServiceProducer = hhServiceProducer;
    }

    @Operation(summary = "Регистрация", description = "Регистрация пользователя через HH API")
    @GetMapping("/registry")
    public ResponseEntity<?> registry(@RequestHeader("Authorization") String authorizationHeader) {
        System.err.println("registry");

        String id = UUID.randomUUID().toString();
        hhServiceProducer.getPendingRequests().put(id, new CompletableFuture<>());

        ResponseEntity<String> response = hhServiceProducer.registry(id, authorizationHeader);
        System.err.println("hh registry: " + response);
        return response;
    }

    @Operation(summary = "Callback", description = "Обработка callback запроса от HH API")
    @GetMapping("/callback")
    public ResponseEntity<?> callback(
            @RequestHeader("Authorization") String authorizationHeader,
            @RequestParam("code") String code) {

        System.err.println("callback");
        String id = UUID.randomUUID().toString();
        hhServiceProducer.getPendingRequests().put(id, new CompletableFuture<>());

        ResponseEntity<String> response = hhServiceProducer.callback(id, authorizationHeader, code);
        System.err.println("hh callback: " + response);
        return response;
    }

    @Operation(summary = "Старт", description = "Запуск процесса в HH API")
    @GetMapping("/start")
    public ResponseEntity<?> start(@RequestHeader("Authorization") String authorizationHeader) {
        System.err.println("start");

        String id = UUID.randomUUID().toString();
        hhServiceProducer.getPendingRequests().put(id, new CompletableFuture<>());

        ResponseEntity<String> response = hhServiceProducer.start(id, authorizationHeader);
        System.err.println("hh start: " + response);
        return response;
    }

    @Operation(summary = "Остановка", description = "Остановка процесса в HH API")
    @GetMapping("/stop")
    public ResponseEntity<?> stop(@RequestHeader("Authorization") String authorizationHeader) {
        System.err.println("stop");

        String id = UUID.randomUUID().toString();
        hhServiceProducer.getPendingRequests().put(id, new CompletableFuture<>());

        ResponseEntity<String> response = hhServiceProducer.stop(id, authorizationHeader);
        System.err.println("hh stop: " + response);
        return response;
    }


    @Operation(summary = "Статус", description = "Получение статуса процесса в HH API")
    @GetMapping("/status")
    public ResponseEntity<?> status(@RequestHeader("Authorization") String authorizationHeader) {
        System.err.println("status");

        String id = UUID.randomUUID().toString();
        hhServiceProducer.getPendingRequests().put(id, new CompletableFuture<>());

        ResponseEntity<String> response = hhServiceProducer.status(id, authorizationHeader);
        System.err.println("hh status: " + response);
        return response;
    }
}