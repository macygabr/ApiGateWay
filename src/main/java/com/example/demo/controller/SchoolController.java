package com.example.demo.controller;

import com.example.demo.models.hhapi.filter.Filter;
import com.example.demo.models.s21.filter.FilterS21;
import com.example.demo.service.SchoolService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/s21")
@Tag(name = "API School21", description = "API для работы с школой 21")
public class SchoolController {
    private final SchoolService schoolService;
    @Operation(summary = "Получение списка пиров")
    @PostMapping("/peers")
    public ResponseEntity<String> peers(@RequestParam(required = false, defaultValue = "0") int page,
                                        @RequestParam(required = false, defaultValue = "25") int size,
                                        @RequestParam(defaultValue = "46e7d965-21e9-4936-bea9-f5ea0d1fddf2") String campusId,
                                        @RequestBody @Validated FilterS21 filter) {

        String response = schoolService.peers(page, size, campusId, filter);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    private String getClientIp(HttpServletRequest request) {
        String ip = request.getHeader("X-Forwarded-For");
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("X-Real-IP");
        }
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        return ip;
    }
}
