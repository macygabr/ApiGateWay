package com.example.demo.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;


@Data
public class AuthenticationServerResponse {
    @JsonProperty("id")
    private String id;

    @JsonProperty("token")
    private String token;
}
