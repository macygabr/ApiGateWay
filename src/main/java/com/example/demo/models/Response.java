package com.example.demo.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class Response {

    @JsonProperty("status")
    private HttpStatus status;
    @JsonProperty("message")
    private String message;

    public Response(String message) {
        try {
            if (message == null) throw new RuntimeException("Message is null");
            Response response = new ObjectMapper().readValue(message, Response.class);
            this.status = response.getStatus();
            this.message = response.getMessage();
        } catch (Exception e) {
            System.err.println("Failed to parse message: " + e.getMessage());
        }
    }

    public String toJson() {
        try {
            return new ObjectMapper().writeValueAsString(this);
        } catch (JsonProcessingException e) {
            throw new HttpException(HttpStatus.CONFLICT, "Error during JSON serialization: "+e.getMessage());
        }
    }
}
