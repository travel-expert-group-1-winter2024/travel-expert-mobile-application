package com.example.travelexpertmobileapplication;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.travelexpertmobileapplication.Models.Customer;
import com.example.travelexpertmobileapplication.utils.ApiClient;
import com.example.travelexpertmobileapplication.utils.ApiEndpoints;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    EditText etUsername, etPassword;
    Button btnLogin;
    TextView tvSignUp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Initialize the views
        etUsername = findViewById(R.id.etLoginEmail);
        etPassword = findViewById(R.id.etLoginPassword);
        btnLogin = findViewById(R.id.btnLogin);
        tvSignUp = findViewById(R.id.tvSignUp);
        fetchCustomers();
        // Set on click listener on the sign up text view
        btnLogin.setOnClickListener(v -> {
            // Get the username and password from the edit text fields
            String username = etUsername.getText().toString();
            String password = etPassword.getText().toString();
            // Create an intent to start the main activity
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            // Start the main activity
            startActivity(intent);
            // Finish the current activity
            finish();
        });
    }

    private void fetchCustomers() {
        ApiEndpoints apiService = ApiClient.getClient().create(ApiEndpoints.class);

        apiService.getCustomers().enqueue(new Callback<JsonElement>() {
            @Override
            public void onResponse(Call<JsonElement> call, Response<JsonElement> response) {
                if (response.isSuccessful() && response.body() != null) {
                    JsonObject jsonObject = response.body().getAsJsonObject();
                    JsonObject embeddedObject = jsonObject.has("_embedded") ? jsonObject.getAsJsonObject("_embedded") : null;

                    if (embeddedObject != null && embeddedObject.has("customers")) {
                        JsonArray customersArray = embeddedObject.getAsJsonArray("customers");
                        List<Customer> customers = new Gson().fromJson(customersArray, new TypeToken<List<Customer>>() {}.getType());

                        // Log customer details
                        customers.forEach(customer -> Log.d("API Response", customer.toString()));
                    } else {
                        Log.e("API Error", "'customers' not found inside '_embedded'");
                    }
                } else {
                    Log.e("API Error", "Response not successful: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<JsonElement> call, Throwable t) {
                Log.e("API Error", "Failed to fetch customers: " + t.getMessage());
            }
        });
    }



    }