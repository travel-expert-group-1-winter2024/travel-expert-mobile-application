package com.example.travelexpertmobileapplication.utils;

import com.example.travelexpertmobileapplication.Models.Customer;
import com.google.gson.JsonElement;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface ApiEndpoints {
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
