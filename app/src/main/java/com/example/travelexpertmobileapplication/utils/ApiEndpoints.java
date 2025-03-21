package com.example.travelexpertmobileapplication.utils;

import com.example.travelexpertmobileapplication.Models.Customer;
import com.google.gson.JsonArray;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface ApiEndpoints {
    @GET("api/customers")
    Call<JsonArray> getCustomers();

    @PUT("api/customers/{id}")
    Call<Customer> updateCustomer(@Path("id") int id, @Body Customer updatedCustomer);

//    // Example GET request with a dynamic ID
//    @GET("users/{id}")
//    Call<User> getUserById(@Path("id") int id);
//
//    // Example POST request
//    @POST("users")
//    Call<User> createUser(@Body User user);
}
