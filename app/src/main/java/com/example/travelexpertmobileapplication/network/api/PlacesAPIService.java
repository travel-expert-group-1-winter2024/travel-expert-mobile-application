package com.example.travelexpertmobileapplication.network.api;

import com.example.travelexpertmobileapplication.model.Place;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface PlacesAPIService {
    @GET("api/places/nearby")
    Call<List<Place>> getNearbyPlaces(
            @Query("lat") double latitude,
            @Query("lng") double longitude,
            @Query("radius") int radius
    );

    @GET("api/places/search")
    Call<ResponseBody> searchPlaces(
            @Query("query") String query,
            @Query("lat") double latitude,
            @Query("lng") double longitude,
            @Query("radius") int radius
    );
}