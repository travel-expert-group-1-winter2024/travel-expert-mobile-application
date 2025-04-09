package com.example.travelexpertmobileapplication.dto.user;

import com.example.travelexpertmobileapplication.dto.generic.GenericApiResponse;

public class AuthResponseDTO<T> extends GenericApiResponse<T> {
    private String token;

    public AuthResponseDTO() {
    }

    public AuthResponseDTO(String token) {
        this.token = token;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
