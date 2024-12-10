package com.example.demo.controller;

import com.example.demo.models.Filter;
import com.example.demo.service.HHService;
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

    @Operation(summary = "Получение ссылки для регистрации")
    @GetMapping("/get-link")
    public ResponseEntity<?> getLink() {
        String response = hhService.getLink();
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @Operation(summary = "Регистрация")
    @PatchMapping("/registry")
    public ResponseEntity<?> registry(@RequestParam("code") String code) {
        String response = hhService.registry();
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }


    @Operation(summary = "Старт", description = "Запуск процесса в HH API")
    @PostMapping("/start")
    public ResponseEntity<?> start(@RequestBody @Validated Filter filter) {
        String response = hhService.start(filter);
        System.err.println("hh start: " + response);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @Operation(summary = "Остановка", description = "Остановка процесса в HH API")
    @GetMapping("/stop")
    public ResponseEntity<?> stop() {
        String response = hhService.stop();
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
//
//
//    @Operation(
//            summary = "Статус",
//            description = "Получение статуса процесса в HH API",
//            parameters = {
//                    @Parameter(
//                            name = "Authorization",
//                            description = "Токен авторизации",
//                            required = true,
//                            in = ParameterIn.HEADER,
//                            schema = @Schema(type = "string", example = "Bearer exampleToken")
//                    )
//            },
//            responses = {
//                    @ApiResponse(responseCode = "200", description = "Статус успешно получен"),
//                    @ApiResponse(responseCode = "401", description = "Неавторизован")
//            }
//    )
//    @GetMapping("/status")
//    public ResponseEntity<?> status(@RequestHeader("Authorization") String authorizationHeader) {
//        System.err.println("status");
//
//        String id = UUID.randomUUID().toString();
//        hhServiceProducer.getPendingRequests().put(id, new CompletableFuture<>());
//
//        ResponseEntity<String> response = hhServiceProducer.status(id, authorizationHeader);
//        System.err.println("hh status: " + response);
//        return response;
//    }
}