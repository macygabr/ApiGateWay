package com.example.demo.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

@Data
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class InfoResponse implements Response {
    @JsonProperty("status")
    private HttpStatus status;
    @JsonProperty("message")
    private String message;

    public InfoResponse(String message) {
        try {
            if (message == null) throw new RuntimeException("Message is null");
            InfoResponse response = new ObjectMapper().readValue(message, InfoResponse.class);
            this.status = response.getStatus();
            this.message = response.getMessage();
        } catch (Exception e) {
            System.err.println("Failed to parse message: " + e.getMessage());
        }
    }
}