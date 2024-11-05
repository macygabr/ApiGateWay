package com.example.demo.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.UUID;

@Data
public class SignInRequest {
    @JsonProperty("id")
    private String id = UUID.randomUUID().toString();

    @JsonProperty("email")
    private String email;

    @JsonProperty("password")
    private String password;
}

