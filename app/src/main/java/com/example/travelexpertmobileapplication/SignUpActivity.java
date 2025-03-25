package com.example.travelexpertmobileapplication;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
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
import com.example.travelexpertmobileapplication.dto.generic.GenericResponse;
import com.example.travelexpertmobileapplication.model.Agency;
import com.example.travelexpertmobileapplication.network.ApiClient;
import com.example.travelexpertmobileapplication.network.api.AgencyAPIService;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignUpActivity extends AppCompatActivity {
    EditText etFirstName, etMiddleInitial, etLastName, etPhoneNumber, etEmail;
    Spinner spinnerAgency;
    Button btnSubmit;
    ImageButton btnBack;
    AgencyAPIService agencyAPIService;
    AgencyListResponse agencyResponse;
    ImageView imgProfile;
    Bitmap bitmapImage;
    ArrayAdapter<Agency> adapter;

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
        spinnerAgency = findViewById(R.id.spnAgency);
        btnSubmit = findViewById(R.id.btnSubmit);
        btnBack = findViewById(R.id.btnBack);
        imgProfile = findViewById(R.id.imgProfile);

        agencyAPIService = ApiClient.getClient().create(AgencyAPIService.class);

        // Back Button Click
        btnBack.setOnClickListener(v -> finish());
        imgProfile.setOnClickListener(v -> showImagePickerDialog());

        loadAgency();
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadAgency();
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
                Log.e("Error", t.getMessage());
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
            Log.e("ImageSave", "Bitmap is null!");
            return;
        }

        String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageName = "image_" + timestamp;

        File storageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        if (storageDir != null && !storageDir.exists()) {
            boolean created = storageDir.mkdirs();
            Log.d("ImageSave", "Pictures folder created: " + created);
        }

        try {
            //create image file
            File imageFile = File.createTempFile(imageName, ".jpg",storageDir);
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

            Log.e("ImageSave", "Image saved to " + imageFile.getAbsolutePath());
            Toast.makeText(this, "Image saved to " + imageFile.getAbsolutePath(), Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            Log.e("ImageSave", "Error saving image", e);
            Toast.makeText(this, "Error saving image", Toast.LENGTH_SHORT).show();
        }
    }
}