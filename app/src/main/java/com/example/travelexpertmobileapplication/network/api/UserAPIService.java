package com.example.travelexpertmobileapplication.network.api;

import com.example.travelexpertmobileapplication.dto.user.SignUpRequestDTO;
import com.example.travelexpertmobileapplication.dto.user.SignUpResponseDTO;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface UserAPIService {
    @POST("/api/signup/agent")
    Call<SignUpResponseDTO> createAgent(@Body SignUpRequestDTO newAgent);
}
