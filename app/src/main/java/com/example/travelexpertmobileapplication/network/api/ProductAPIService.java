package com.example.travelexpertmobileapplication.network.api;

import com.google.gson.JsonArray;

import retrofit2.Call;
import retrofit2.http.GET;

public interface ProductAPIService {
    @GET("/api/products")
    Call<JsonArray> getProducts();
}
