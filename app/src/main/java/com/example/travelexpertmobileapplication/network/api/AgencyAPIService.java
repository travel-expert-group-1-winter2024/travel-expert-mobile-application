package com.example.travelexpertmobileapplication.network.api;

import com.example.travelexpertmobileapplication.dto.agency.AgencyListResponse;

import retrofit2.Call;
import retrofit2.http.GET;

public interface AgencyAPIService {

    @GET("/agencies")
    Call<AgencyListResponse> getAllAgencies();

}
