package com.round3.realestate.payload;


import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AuthResponse {

    private boolean success;
    private String message;
    private String token;

    public AuthResponse(boolean success, String message) {
        this.success = success;
        this.message = message;
    }
}