package com.example.travelexpertmobileapplication;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.travelexpertmobileapplication.dto.agency.AgencyListResponse;
import com.example.travelexpertmobileapplication.dto.agent.CreateAgentRequestDTO;
import com.example.travelexpertmobileapplication.dto.user.SignUpRequestDTO;
import com.example.travelexpertmobileapplication.dto.user.SignUpResponseDTO;
import com.example.travelexpertmobileapplication.model.Agency;
import com.example.travelexpertmobileapplication.network.ApiClient;
import com.example.travelexpertmobileapplication.network.api.AgencyAPIService;
import com.example.travelexpertmobileapplication.network.api.AgentAPIService;
import com.example.travelexpertmobileapplication.network.api.UserAPIService;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import timber.log.Timber;

public class SignUpActivity extends AppCompatActivity {
    EditText etFirstName, etMiddleInitial, etLastName, etPhoneNumber, etEmail, etPassword;
    Spinner spinnerAgency;
    Button btnSubmit;
    ImageButton btnBack;
    AgencyAPIService agencyAPIService;
    AgentAPIService agentAPIService;
    UserAPIService userAPIService;
    AgencyListResponse agencyResponse;
    ImageView imgProfile;
    Bitmap bitmapImage;
    ArrayAdapter<Agency> adapter;
    String imagePath;

    private static final int REQUEST_IMAGE_CAPTURE = 1;
    private static final int REQUEST_IMAGE_PICK = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_sign_up);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Initialize UI components
        etFirstName = findViewById(R.id.etFirstName);
        etMiddleInitial = findViewById(R.id.etMiddleInitial);
        etLastName = findViewById(R.id.etLastName);
        etPhoneNumber = findViewById(R.id.etPhoneNumber);
        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        spinnerAgency = findViewById(R.id.spnAgency);
        btnSubmit = findViewById(R.id.btnSubmit);
        btnBack = findViewById(R.id.btnBack);
        imgProfile = findViewById(R.id.imgProfile);

        agencyAPIService = ApiClient.getClient().create(AgencyAPIService.class);
        agentAPIService = ApiClient.getClient().create(AgentAPIService.class);
        userAPIService = ApiClient.getClient().create(UserAPIService.class);

        // Back Button Click
        btnBack.setOnClickListener(v -> finish());
        imgProfile.setOnClickListener(v -> showImagePickerDialog());
        btnSubmit.setOnClickListener(v -> submit());

        loadAgency();
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadAgency();
    }

    // handle submit button click
    private void submit() {
        String firstName = etFirstName.getText().toString();
        String middleInitial = etMiddleInitial.getText().toString();
        String lastName = etLastName.getText().toString();
        String phoneNumber = etPhoneNumber.getText().toString();
        String email = etEmail.getText().toString();
        String password = etPassword.getText().toString();
        Agency agency = (Agency) spinnerAgency.getSelectedItem();

        if (firstName.isEmpty() || lastName.isEmpty() || phoneNumber.isEmpty() || email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        // create user and agent
        createAgentAndUser(firstName, middleInitial, lastName, phoneNumber, email, password, agency);
    }

    private void createAgentAndUser(String firstName, String middleInitial, String lastName, String phoneNumber, String email, String password, Agency agency) {
        Call<Void> callAgent = agentAPIService.createAgent(
                new CreateAgentRequestDTO(firstName, middleInitial, lastName, phoneNumber, email, (long) agency.getId())
        );
        callAgent.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    String location = response.headers().get("Location");
                    int agentId = extractAgentIdFromLocationHeader(location);

                    // create user
                    createUser(email, password, agentId);

                    if (agentId != -1 && imagePath != null) {
                        // upload image
                        uploadImage(imagePath, agentId);
                    }

                    Toast.makeText(SignUpActivity.this, "Agent registered successfully", Toast.LENGTH_SHORT).show();
                    Timber.i("Agent registered successfully");
                    finish();
                } else {
                    Toast.makeText(SignUpActivity.this, "Failed to register agent", Toast.LENGTH_SHORT).show();
                    Timber.e("Failed to register agent: %s", response.code());
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(SignUpActivity.this, "Failed to register agent", Toast.LENGTH_SHORT).show();
                Timber.e(t, "Failed to register agent");
            }
        });
    }

    private void createUser(String email, String password, int agentId) {
        Call<SignUpResponseDTO> createAgentCall = userAPIService.createAgent(new SignUpRequestDTO(email, password, agentId));
        createAgentCall.enqueue(new Callback<SignUpResponseDTO>() {
            @Override
            public void onResponse(Call<SignUpResponseDTO> call, Response<SignUpResponseDTO> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Timber.d("User created successfully");
                } else {
                    Toast.makeText(SignUpActivity.this, "Failed to register user", Toast.LENGTH_SHORT).show();
                    Timber.e("Failed to register user: %s", response.code());
                }
            }
            @Override
            public void onFailure(Call<SignUpResponseDTO> call, Throwable t) {
                Toast.makeText(SignUpActivity.this, "Failed to register user", Toast.LENGTH_SHORT).show();
                Timber.e(t, "Failed to register user");
            }
        });
    }

    private void uploadImage(String imagePath, int agentId) {

        File file = new File(imagePath);
        RequestBody requestFile = RequestBody.create(MediaType.parse("image/jpeg"), file);
        MultipartBody.Part body = MultipartBody.Part.createFormData("image", file.getName(), requestFile);

        Call<ResponseBody> call = agentAPIService.uploadAgentPhoto(agentId, body);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    Timber.i("Image uploaded successfully");
                } else {
                    Timber.e("Failed to upload image: %s", response.code());
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Timber.e(t, "Failed to upload image");
            }
        });
    }

    private void loadAgency() {
        agencyAPIService.getAllAgencies().enqueue(new Callback<AgencyListResponse>() {
            @Override
            public void onResponse(Call<AgencyListResponse> call, Response<AgencyListResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    agencyResponse = response.body();
                    adapter = new ArrayAdapter<>(
                            SignUpActivity.this,
                            android.R.layout.simple_spinner_item,
                            agencyResponse.getEmbedded().agencies
                    );
                    spinnerAgency.setAdapter(adapter);
                }
            }

            @Override
            public void onFailure(Call<AgencyListResponse> call, Throwable t) {
                Timber.e(t, "Failed to fetch agencies");
            }
        });
    }

    private void showImagePickerDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Select Image")
                .setItems(new CharSequence[]{"Take Photo", "Choose from Gallery"}, (dialog, which) -> {
                    if (which == 0) {
                        if (checkPermission()) {
                            Toast.makeText(this, "Permission granted", Toast.LENGTH_SHORT).show();
                            captureImage();
                        } else {
                            // if granted, capture image
                            Toast.makeText(this, "Permission not granted, request permission", Toast.LENGTH_SHORT).show();
                            requestPermission();
                        }
                    } else {
                        selectImageFromGallery(); // Open Gallery
                    }
                })
                .show();
    }

    private void selectImageFromGallery() {
        Intent pickPhoto = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        pickPhoto.setType("image/*");
        startActivityForResult(pickPhoto, REQUEST_IMAGE_PICK);
    }

    private Boolean checkPermission() {
        // camera's permission
        int cameraPermission = ContextCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.CAMERA);
        // storage's permission
        int storagePermission = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);

        return cameraPermission == PackageManager.PERMISSION_GRANTED
                && storagePermission == PackageManager.PERMISSION_GRANTED;
    }

    private void captureImage() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        } else {
            Toast.makeText(this, "No camera app installed", Toast.LENGTH_SHORT).show();
            Timber.e("No camera app installed");
        }
    }

    private void requestPermission() {
        final int PERMISSION_CODE = 200;
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}, PERMISSION_CODE);
    }

    // process result of permission
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (grantResults.length > 0) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // permission is granted to camera, call capture image
                Toast.makeText(this, "Permission granted to camera", Toast.LENGTH_SHORT).show();
                captureImage();
            } else {
                Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show();
                requestPermission();
            }
        }
    }

    // process image that is captured
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_IMAGE_CAPTURE) {
                // Image captured from camera
                Bundle extras = data.getExtras();
                Bitmap imageBitmap = (Bitmap) extras.get("data");
                imgProfile.setImageBitmap(imageBitmap);
                saveImageToDrive(imageBitmap);
            } else if (requestCode == REQUEST_IMAGE_PICK) {
                // Image selected from gallery
                Uri selectedImageUri = data.getData();
                imgProfile.setImageURI(selectedImageUri);
            }
        }
    }

    private void saveImageToDrive(Bitmap bitmapImage) {
        if (bitmapImage == null) {
            Timber.e("Bitmap image is null");
            return;
        }

        String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageName = "image_" + timestamp;

        File storageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        if (storageDir != null && !storageDir.exists()) {
            boolean created = storageDir.mkdirs();
            Timber.i("Pictures folder created: %s", created);
        }

        try {
            //create image file
            File imageFile = File.createTempFile(imageName, ".jpg", storageDir);
            //write image to file
            FileOutputStream fos = new FileOutputStream(imageFile);
            bitmapImage.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            fos.flush();
            fos.close();

            // Make image visible in Gallery
            Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
            Uri contentUri = Uri.fromFile(imageFile);
            mediaScanIntent.setData(contentUri);
            sendBroadcast(mediaScanIntent);

            imagePath = imageFile.getAbsolutePath();

            Toast.makeText(this, "Image saved to " + imageFile.getAbsolutePath(), Toast.LENGTH_SHORT).show();
            Timber.i("Image saved to %s", imageFile.getAbsolutePath());
        } catch (IOException e) {
            Toast.makeText(this, "Error saving image", Toast.LENGTH_SHORT).show();
            Timber.e(e, "Error saving image");
        }
    }

    private int extractAgentIdFromLocationHeader(String location) {
        if (location == null || location.isEmpty()) return -1;
        try {
            String[] parts = location.split("/");
            return Integer.parseInt(parts[parts.length - 1]);
        } catch (Exception e) {
            Timber.e("Failed to parse agent ID from location: %s", location);
            return -1;
        }
    }
}