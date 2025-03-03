package org.example.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.example.service.StatisticsService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.http.HttpResponse;

@RestController
@RequestMapping("/statistics")
@RequiredArgsConstructor
@Tag(name = "Статистика", description = "Получение статистики")
public class StatisticsController {
    private final StatisticsService statisticsService;

    @Operation(summary = "Получение списка пиров")
    @GetMapping("/peers")
    public ResponseEntity<String> getPeers() {
        return statisticsService.peers(0,0,"12");
    }
}