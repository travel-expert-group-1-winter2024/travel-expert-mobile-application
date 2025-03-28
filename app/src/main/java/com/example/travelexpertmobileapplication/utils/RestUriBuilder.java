package com.example.travelexpertmobileapplication.utils;

public class RestUriBuilder {
    // this for backend base server. it will be different from Retrofit base url
    private static final String BASE_URL = "http://localhost:8080/";

    public static String agencyUri(long agencyId) {
        return BASE_URL + "agencies/" + agencyId;
    }
    public static String userUri(String userId) {
        return BASE_URL + "users/" + userId;
    }
}
