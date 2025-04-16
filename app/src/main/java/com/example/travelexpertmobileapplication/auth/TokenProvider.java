package com.example.travelexpertmobileapplication.auth;

public class TokenProvider {
    private static String token;

    public static String getToken() {
        return token;
    }

    public static void setToken(String newToken) {
        token = newToken;
    }
}
