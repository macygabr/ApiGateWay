package com.example.demo.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class SignUpRequest {
    @JsonProperty("email")
    private String email;
    @JsonProperty("password")
    private String password;
}

