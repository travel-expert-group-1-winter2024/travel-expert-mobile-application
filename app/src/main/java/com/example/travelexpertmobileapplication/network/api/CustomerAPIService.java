package com.example.travelexpertmobileapplication.network.api;

import com.google.gson.JsonElement;

import retrofit2.Call;
import retrofit2.http.GET;

public interface CustomerAPIService {
    @GET("customers")
    Call<JsonElement> getCustomers();

//    // Example GET request with a dynamic ID
//    @GET("users/{id}")
//    Call<User> getUserById(@Path("id") int id);
//
//    // Example POST request
//    @POST("users")
//    Call<User> createUser(@Body User user);
}
