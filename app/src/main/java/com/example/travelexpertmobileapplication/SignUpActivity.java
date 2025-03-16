package com.example.travelexpertmobileapplication;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class SignUpActivity extends AppCompatActivity {
    EditText etFirstName, etMiddleInitial, etLastName, etPhoneNumber, etEmail;
    Spinner spinnerAgency;
    Button btnSubmit;
    ImageButton btnBack;
    ImageView imgProfile;
    Bitmap bitmapImage;

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

        // Back Button Click
        btnBack.setOnClickListener(v -> finish());
        imgProfile.setOnClickListener(v -> openCamera());
    }

    private void openCamera() {
        if (checkPermission()) {
            Toast.makeText(this, "Permission granted", Toast.LENGTH_SHORT).show();
            captureImage();
        } else {
            // if granted, capture image
            Toast.makeText(this, "Permission not granted, request permission", Toast.LENGTH_SHORT).show();
            requestPermission();
        }
    }

    private Boolean checkPermission() {
        // camera's permission
        int cameraPermission = ContextCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA);
        // storage's permission
        int storagePermission = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);

        return cameraPermission == PackageManager.PERMISSION_GRANTED
                && storagePermission == PackageManager.PERMISSION_GRANTED;
    }

    private void captureImage() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, 1);
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
            } else { // permission denied
                // request for permission again
                Toast.makeText(this, "Permission to camera is denied", Toast.LENGTH_SHORT).show();
//                requestPermission();
            }
        }
    }

    // process image that is captured
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            // extract image from intent using extras
            Bundle extras = data.getExtras();
            // TODO: We might don't need to create a new attribute for bitmap image
            bitmapImage = (Bitmap) extras.get("data");
            imgProfile.setImageBitmap(bitmapImage);
            // store image
            saveImageToDrive(bitmapImage);
        }
    }

    private void saveImageToDrive(Bitmap bitmapImage) {
        String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageName = "image_" + timestamp + ".jpg";

        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File imageFile = new File(storageDir, imageName);

        try (FileOutputStream fos = new FileOutputStream(imageFile)) {
            bitmapImage.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            Toast.makeText(this, "Image saved to " + imageFile.getAbsolutePath(), Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(this, "Error saving image", Toast.LENGTH_SHORT).show();
        }
    }
}