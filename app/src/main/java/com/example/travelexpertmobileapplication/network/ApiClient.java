package com.example.travelexpertmobileapplication.network;

import com.example.travelexpertmobileapplication.auth.TokenProvider;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiClient {
    private static final String BASE_URL = "http://10.0.2.2:8080/";  // Change this to your API base URL
    private static Retrofit retrofit = null;

    // Singleton Retrofit instance
    public static Retrofit getClient() {
        if (retrofit == null) {
            HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
            loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
            // Attach it to OkHttpClient
            OkHttpClient client = new OkHttpClient.Builder()
                    .addInterceptor(chain -> {
                        okhttp3.Request original = chain.request();
                        okhttp3.Request.Builder builder = original.newBuilder();

                        String token = TokenProvider.getToken();
                        if (token != null && !token.isEmpty()) {
                            builder.header("Authorization", "Bearer " + token);
                        }

                        okhttp3.Request request = builder.build();
                        return chain.proceed(request);
                    })
                    .addInterceptor(loggingInterceptor)
                    .build();

            // Retrofit with logging-enabled client
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .client(client)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }
}
