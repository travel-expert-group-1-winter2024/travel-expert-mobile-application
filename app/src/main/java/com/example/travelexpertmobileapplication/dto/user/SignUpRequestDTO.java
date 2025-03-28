package com.example.travelexpertmobileapplication.dto.user;

public class SignUpRequestDTO {
    private String username;
    private String password;
    private Integer agentId;

    public SignUpRequestDTO() {
    }

    public SignUpRequestDTO(String username, String password, Integer agentId) {
        this.username = username;
        this.password = password;
        this.agentId = agentId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Integer getAgentId() {
        return agentId;
    }

    public void setAgentId(Integer agentId) {
        this.agentId = agentId;
    }
}
