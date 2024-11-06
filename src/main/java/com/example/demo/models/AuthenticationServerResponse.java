package com.example.demo.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AuthenticationServerResponse {

    @JsonProperty("status")
    private Boolean status = false;

    @JsonProperty("id")
    private String id = "0";

    @JsonProperty("user_id")
    private Long user_id = 0L;

    @JsonProperty("token")
    private String token = "0";

    @JsonProperty("token_name")
    private String tokenName = "";

    public AuthenticationServerResponse(String message) {
        try {
            if (message == null) throw new RuntimeException("Message is null");
            AuthenticationServerResponse response = new ObjectMapper().readValue(message, AuthenticationServerResponse.class);
            this.status = response.getStatus();
            this.id = response.getId();
            this.token = response.getToken();
            this.tokenName = response.getTokenName();
        } catch (Exception e) {
            System.err.println("Failed to parse message: " + e.getMessage());
        }
    }

    @Override
    public String toString() {
        try {
            return new ObjectMapper().writeValueAsString(this);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Failed to convert to JSON string", e);
        }
    }
}
