package com.example.demo.models;


import lombok.Data;
import org.springframework.http.HttpStatus;

@Data
public class HttpException extends RuntimeException {
    private HttpStatus status;

    public HttpException(HttpStatus errorCode, String message) {
        super(message);
        this.status = errorCode;
    }
}
