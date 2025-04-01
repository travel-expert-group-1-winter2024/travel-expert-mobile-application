package com.example.travelexpertmobileapplication.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.travelexpertmobileapplication.R;

public class ProductDetailsFragment extends Fragment {

    private EditText etProductId, etProductName;
    private Button btnEdit, btnSave;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_product_details, container, false);

        // Initialize views
        etProductId = view.findViewById(R.id.etProductId);
        etProductName = view.findViewById(R.id.etProductName);
        btnEdit = view.findViewById(R.id.btnEdit);
        btnSave = view.findViewById(R.id.btnSave);

        // Get arguments
        Bundle args = getArguments();
        if (args != null) {
            int productId = args.getInt("productId");
            String productName = args.getString("productName");

            etProductId.setText(String.valueOf(productId));
            etProductName.setText(productName);
        }

        // Set inputs disabled by default
        setInputsEnabled(false);

        // Edit button logic
        btnEdit.setOnClickListener(v -> setInputsEnabled(true));

        // Save button logic
        btnSave.setOnClickListener(v -> setInputsEnabled(false));

        return view;
    }

    private void setInputsEnabled(boolean enabled) {
        etProductId.setEnabled(false); // Product ID stays readonly
        etProductName.setEnabled(enabled);
    }
}
