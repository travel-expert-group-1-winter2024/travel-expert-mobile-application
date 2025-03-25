package com.example.travelexpertmobileapplication.network.api;

import com.example.travelexpertmobileapplication.dto.agent.CreateAgentRequestDTO;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface AgentAPIService {
    @POST("/agents")
    Call<Void> createAgent(@Body CreateAgentRequestDTO agent);
}
