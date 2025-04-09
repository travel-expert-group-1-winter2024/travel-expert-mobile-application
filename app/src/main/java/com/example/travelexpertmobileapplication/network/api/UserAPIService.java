package com.example.travelexpertmobileapplication.network.api;

import com.example.travelexpertmobileapplication.dto.agent.AgentDetailsResponseDTO;
import com.example.travelexpertmobileapplication.dto.generic.GenericApiResponse;
import com.example.travelexpertmobileapplication.dto.user.AuthResponseDTO;
import com.example.travelexpertmobileapplication.dto.user.LoginRequestDTO;
import com.example.travelexpertmobileapplication.dto.user.LoginResponseDTO;
import com.example.travelexpertmobileapplication.dto.user.SignUpRequestDTO;
import com.example.travelexpertmobileapplication.dto.user.SignUpResponseDTO;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface UserAPIService {
    @POST("/api/signup/agent")
    Call<SignUpResponseDTO> createAgent(@Body SignUpRequestDTO newAgent);

    @POST("/api/login")
    Call<AuthResponseDTO<LoginResponseDTO>> login(@Body LoginRequestDTO user);

    @GET("/agents/me")
    Call<GenericApiResponse<AgentDetailsResponseDTO>> agentDetails(@Body String username);
}
