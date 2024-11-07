package com.example.demo.models.signIn;

import com.example.demo.models.HttpException;
import com.example.demo.models.UserData;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;


@Data
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class SignInResponse {
    @JsonProperty("status")
    private HttpStatus status;
    @JsonProperty("message")
    private String message;

    @JsonProperty("token")
    private String token;
    @JsonProperty("token_name")
    private String tokenName;

    public SignInResponse(String message) {
        try {
            if (message == null) throw new HttpException(HttpStatus.BAD_REQUEST,"Message is null");
            UserData response = new ObjectMapper().readValue(message, UserData.class);
            this.status = response.getStatus();
            this.message = response.getMessage();
            this.token = response.getToken();
            this.tokenName = response.getTokenName();
        } catch (Exception e) {
            throw new HttpException(HttpStatus.BAD_REQUEST,"Failed to parse message: "+ e);
        }
    }

    @Override
    public String toString() {
        try {
            return (new ObjectMapper()).writeValueAsString(this);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}