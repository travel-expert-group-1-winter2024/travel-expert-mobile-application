package com.example.travelexpertmobileapplication.network.api;

import com.example.travelexpertmobileapplication.model.Product;
import com.google.gson.JsonArray;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface ProductAPIService {
    @GET("/api/products")
    Call<JsonArray> getProducts();

    @PUT("/api/products/{id}")
    Call<Product> updateProduct(@Path("id") int id, @Body Product product);
}
