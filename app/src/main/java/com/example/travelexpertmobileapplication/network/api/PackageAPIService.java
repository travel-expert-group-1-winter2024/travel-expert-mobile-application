package com.example.travelexpertmobileapplication.network.api;

import com.example.travelexpertmobileapplication.model.Package;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface PackageAPIService {

    @GET("/packages")
    Call<JsonArray> getPackages();

    @POST("/packages")
    Call<com.example.travelexpertmobileapplication.model.Package> createPackage(@Body JsonObject newPackage);

    @GET("/packages/product-supplier")
    Call<JsonArray> getAllProductSuppliers();

    @PUT("/packages/{id}")
    Call<Package> updatePackage(@Path("id") int id, @Body JsonObject updatedPackage);

    @DELETE("/packages/{id}")
    Call<Void> deletePackage(@Path("id") int id);
}
