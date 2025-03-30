package com.example.travelexpertmobileapplication.network.api;

import com.example.travelexpertmobileapplication.model.PastTripsModel;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface BookingDetailAPIService {
    @GET("api/bookingdetails")
    Call<List<PastTripsModel>> getPastTrips();
}
