package com.example.travelexpertmobileapplication;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.travelexpertmobileapplication.auth.TokenProvider;
import com.example.travelexpertmobileapplication.dto.agent.AgentDetailsResponseDTO;
import com.example.travelexpertmobileapplication.dto.generic.GenericApiResponse;
import com.example.travelexpertmobileapplication.dto.user.AuthResponseDTO;
import com.example.travelexpertmobileapplication.dto.user.LoginRequestDTO;
import com.example.travelexpertmobileapplication.dto.user.LoginResponseDTO;
import com.example.travelexpertmobileapplication.network.ApiClient;
import com.example.travelexpertmobileapplication.network.api.AgentAPIService;
import com.example.travelexpertmobileapplication.network.api.UserAPIService;
import com.example.travelexpertmobileapplication.utils.SharedPrefUtil;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import timber.log.Timber;

public class LoginActivity extends AppCompatActivity {

    EditText etUsername, etPassword;
    Button btnLogin;
    TextView tvSignUp;
    UserAPIService userAPIService;
    String token;

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

        String previousToken = SharedPrefUtil.getToken(this);
        if (previousToken != null) {
            SharedPrefUtil.clearToken(this);
        }

        // Initialize the views
        etUsername = findViewById(R.id.etLoginEmail);
        etPassword = findViewById(R.id.etLoginPassword);
        btnLogin = findViewById(R.id.btnLogin);
        tvSignUp = findViewById(R.id.tvSignUp);

        userAPIService = ApiClient.getClient().create(UserAPIService.class);

        // Set on click listener on the sign up text view
        btnLogin.setOnClickListener(v -> {
            // Get the username and password from the edit text fields
            String username = etUsername.getText().toString().toLowerCase();
            String password = etPassword.getText().toString();

            if (username.isEmpty() || password.isEmpty()) {
                // Show an error message if the username or password is empty
                Toast.makeText(LoginActivity.this, "Username or password is empty", Toast.LENGTH_SHORT).show();
                return;
            }

            Timber.i("Logging with user %s", username);
            Call<AuthResponseDTO<LoginResponseDTO>> call = userAPIService.login(new LoginRequestDTO(username, password));
            call.enqueue(new Callback<>() {
                @Override
                public void onResponse(Call<AuthResponseDTO<LoginResponseDTO>> call, Response<AuthResponseDTO<LoginResponseDTO>> response) {
                    if (response.body() != null && response.body().getData() != null) {
                        Timber.d("This is the response body %s:", response.body());
                        String[] roles = response.body().getData().getRole();
                        Timber.d("Roles: %s", (Object) roles);
                        if (roles != null && roles.length == 1 && "CUSTOMER".equalsIgnoreCase(roles[0])) {
                            Toast.makeText(LoginActivity.this, "Customers cannot log in here.", Toast.LENGTH_SHORT).show();
                            return;
                        }

                        token = response.body().getToken();
                        getAgentInfo();
                        Timber.d("Login successful, token: %s", token);
                        SharedPrefUtil.saveToken(LoginActivity.this, token);
                        TokenProvider.setToken(token);

                        // Create an intent to start the main activity
                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);

                        // Start the main activity
                        startActivity(intent);

                        // Finish the current activity
                        finish();
                    } else {
                        Toast.makeText(LoginActivity.this, "Failed to login", Toast.LENGTH_SHORT).show();
                        Timber.e("Failed to login: Response body is null");
                    }
                }

                @Override
                public void onFailure
                        (Call<AuthResponseDTO<LoginResponseDTO>> call, Throwable t) {
                    Toast.makeText(LoginActivity.this, "Failed to login", Toast.LENGTH_SHORT).show();
                    Timber.e(t, "Failed to login");
                }
            });
        });

        tvSignUp.setOnClickListener(v -> {
            // Create an intent to start the sign up activity
            Intent intent = new Intent(LoginActivity.this, SignUpActivity.class);
            // Start the sign up activity
            startActivity(intent);
        });
    }

    public void getAgentInfo() {
        AgentAPIService agentAPIService = ApiClient.getClient().create(AgentAPIService.class);
        Call<GenericApiResponse<AgentDetailsResponseDTO>> call = agentAPIService.getMyAgentInfo("Bearer " + token);
        call.enqueue(new Callback<>() {
            @Override
            public void onResponse(Call<GenericApiResponse<AgentDetailsResponseDTO>> call, Response<GenericApiResponse<AgentDetailsResponseDTO>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    AgentDetailsResponseDTO agentInfo = response.body().getData();
                    SharedPrefUtil.saveAgentId(LoginActivity.this, agentInfo.getId());
                }
            }

            @Override
            public void onFailure(Call<GenericApiResponse<AgentDetailsResponseDTO>> call, Throwable t) {
                Timber.tag("onFailure:").e("Api call failed: %s", t.getMessage());
            }
        });
    }
}