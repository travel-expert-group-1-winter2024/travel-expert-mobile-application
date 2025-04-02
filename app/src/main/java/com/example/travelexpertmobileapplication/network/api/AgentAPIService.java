package com.example.travelexpertmobileapplication.network.api;

import com.example.travelexpertmobileapplication.dto.agent.CreateAgentRequestDTO;
import com.example.travelexpertmobileapplication.model.Agent;

import okhttp3.MultipartBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;

public interface AgentAPIService {
    @POST("/agents")
    Call<Void> createAgent(@Body CreateAgentRequestDTO agent);
    @Multipart
    @POST("/agents/{id}/upload")
    Call<ResponseBody> uploadAgentPhoto(
            @Path("id") int agentId,
            @Part MultipartBody.Part image
    );

    @GET("/api/agents/me")
    Call<Agent> getMyAgentInfo(@Header("Authorization") String token);





} //class
