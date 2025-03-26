package com.example.travelexpertmobileapplication.network.api;

import com.example.travelexpertmobileapplication.model.Customer;
import com.google.gson.JsonArray;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface CustomerAPIService {
    @GET("api/customers")
    Call<JsonArray> getCustomers();

    @PUT("api/customers/{id}")
    Call<Customer> updateCustomer(@Path("id") int id, @Body Customer updatedCustomer);


}
