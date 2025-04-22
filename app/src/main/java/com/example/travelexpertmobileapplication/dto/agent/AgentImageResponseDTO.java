package com.example.travelexpertmobileapplication.dto.agent;

public class AgentImageResponseDTO {
    private String imageURL;

    public AgentImageResponseDTO() {
    }

    public AgentImageResponseDTO(String imageUrl) {
        this.imageURL = imageUrl;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }
}
