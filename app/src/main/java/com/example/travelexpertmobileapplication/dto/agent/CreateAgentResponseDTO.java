package com.example.travelexpertmobileapplication.dto.agent;

public class CreateAgentResponseDTO {
    // agent table
    private Integer agentId;
    private String agtFirstName;
    private String agtMiddleInitial;
    private String agtLastName;
    private String agtBusPhone;
    private String agtEmail;
    private String agtPosition;
    // agency table
    private String agencyName;
    // user table
    private String userId;
    private String username;
    private String role;
    // customer table
    private Integer customerId;
    private String custFirstName;
    private String custLastName;
    private String custBusPhone;
    private String custEmail;

    public CreateAgentResponseDTO() {
    }

    public Integer getAgentId() {
        return agentId;
    }

    public String getAgtFirstName() {
        return agtFirstName;
    }

    public String getAgtMiddleInitial() {
        return agtMiddleInitial;
    }

    public String getAgtLastName() {
        return agtLastName;
    }

    public String getAgtBusPhone() {
        return agtBusPhone;
    }

    public String getAgtEmail() {
        return agtEmail;
    }

    public String getAgtPosition() {
        return agtPosition;
    }

    public String getAgencyName() {
        return agencyName;
    }

    public String getUserId() {
        return userId;
    }

    public String getUsername() {
        return username;
    }

    public String getRole() {
        return role;
    }

    public Integer getCustomerId() {
        return customerId;
    }

    public String getCustFirstName() {
        return custFirstName;
    }

    public String getCustLastName() {
        return custLastName;
    }

    public String getCustBusPhone() {
        return custBusPhone;
    }

    public String getCustEmail() {
        return custEmail;
    }
}
