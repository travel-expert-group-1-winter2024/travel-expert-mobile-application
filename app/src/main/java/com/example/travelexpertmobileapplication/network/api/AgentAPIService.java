package com.example.travelexpertmobileapplication.network.api;

import com.example.travelexpertmobileapplication.dto.agent.AgentDetailsResponseDTO;
import com.example.travelexpertmobileapplication.dto.agent.AgentUpdateDetailRequestDTO;
import com.example.travelexpertmobileapplication.dto.agent.CreateAgentRequestDTO;
import com.example.travelexpertmobileapplication.dto.agent.CreateAgentResponseDTO;
import com.example.travelexpertmobileapplication.dto.generic.GenericApiResponse;

import okhttp3.MultipartBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.Path;

public interface AgentAPIService {
    @POST("/agents")
    Call<GenericApiResponse<CreateAgentResponseDTO>> createAgent(@Body CreateAgentRequestDTO agent);

    @Multipart
    @POST("/agents/{id}/upload")
    Call<ResponseBody> uploadAgentPhoto(
            @Path("id") int agentId,
            @Part MultipartBody.Part image
    );

    @GET("/agents/me")
    Call<GenericApiResponse<AgentDetailsResponseDTO>> getMyAgentInfo(@Header("Authorization") String token);

    @PUT("/agents/{id}")
    Call<GenericApiResponse<AgentDetailsResponseDTO>> updateAgentInfo(
            @Header("Authorization") String token,
            @Path("id") long id,
            @Body AgentUpdateDetailRequestDTO agentInfo);

    @Headers("Accept: image/jpeg")
    @GET("/agents/{id}/photo")
    Call<ResponseBody> getAgentPhoto(
            @Header("Authorization") String token,
            @Path("id") int agentId);
}
