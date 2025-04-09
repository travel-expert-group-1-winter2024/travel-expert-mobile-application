package com.example.travelexpertmobileapplication.dto.user;

import java.util.UUID;

public class LoginResponseDTO {
    private UUID id;
    private String name;
    private String email;
    private String[] roles;

    public LoginResponseDTO() {
    }

    public LoginResponseDTO(UUID id, String name, String email, String[] roles) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.roles = roles;
    }

    public UUID getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String[] getRoles() {
        return roles;
    }
}
