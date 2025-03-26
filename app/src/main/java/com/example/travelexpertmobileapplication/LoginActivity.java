package com.example.travelexpertmobileapplication;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

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

        tvSignUp.setOnClickListener(v -> {
            // Create an intent to start the sign up activity
            Intent intent = new Intent(LoginActivity.this, SignUpActivity.class);
            // Start the sign up activity
            startActivity(intent);
        });
    }





    }