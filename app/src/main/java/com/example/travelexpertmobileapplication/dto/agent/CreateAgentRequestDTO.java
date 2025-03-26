package com.example.travelexpertmobileapplication.dto.agent;

import com.example.travelexpertmobileapplication.utils.RestUriBuilder;

public class CreateAgentRequestDTO {
    private String agtFirstName;
    private String agtMiddleInitial;
    private String agtLastName;
    private String agtBusPhone;
    private String agtEmail;
    private String agtPosition;
    private String user; // if needed
    private String agency; // need to be String for HATEOAS format

    public CreateAgentRequestDTO() {
    }

    // without user
    public CreateAgentRequestDTO(String agtFirstName, String agtMiddleInitial, String agtLastName, String agtBusPhone, String agtEmail, Long agencyId) {
        this.agtFirstName = agtFirstName;
        this.agtMiddleInitial = agtMiddleInitial;
        this.agtLastName = agtLastName;
        this.agtBusPhone = agtBusPhone;
        this.agtEmail = agtEmail;
        this.agtPosition = "Agent"; // default value
        this.agency = RestUriBuilder.agencyUri(agencyId);
    }

    public CreateAgentRequestDTO(String agtFirstName, String agtMiddleInitial, String agtLastName, String agtBusPhone, String agtEmail,  String user, Long agencyId) {
        this.agtFirstName = agtFirstName;
        this.agtMiddleInitial = agtMiddleInitial;
        this.agtLastName = agtLastName;
        this.agtBusPhone = agtBusPhone;
        this.agtEmail = agtEmail;
        this.agtPosition = "Agent"; // default value;
        this.user = user;
        this.agency = RestUriBuilder.agencyUri(agencyId);
    }

    public String getAgtFirstName() {
        return agtFirstName;
    }

    public void setAgtFirstName(String agtFirstName) {
        this.agtFirstName = agtFirstName;
    }

    public String getAgtMiddleInitial() {
        return agtMiddleInitial;
    }

    public void setAgtMiddleInitial(String agtMiddleInitial) {
        this.agtMiddleInitial = agtMiddleInitial;
    }

    public String getAgtLastName() {
        return agtLastName;
    }

    public void setAgtLastName(String agtLastName) {
        this.agtLastName = agtLastName;
    }

    public String getAgtBusPhone() {
        return agtBusPhone;
    }

    public void setAgtBusPhone(String agtBusPhone) {
        this.agtBusPhone = agtBusPhone;
    }

    public String getAgtEmail() {
        return agtEmail;
    }

    public void setAgtEmail(String agtEmail) {
        this.agtEmail = agtEmail;
    }

    public String getAgtPosition() {
        return agtPosition;
    }

    public void setAgtPosition(String agtPosition) {
        this.agtPosition = agtPosition;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getAgency() {
        return agency;
    }

    public void setAgency(String agency) {
        this.agency = agency;
    }
}
