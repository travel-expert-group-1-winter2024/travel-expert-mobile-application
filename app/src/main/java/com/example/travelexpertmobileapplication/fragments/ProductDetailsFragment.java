package com.example.travelexpertmobileapplication.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.travelexpertmobileapplication.R;
import com.example.travelexpertmobileapplication.model.Product;
import com.example.travelexpertmobileapplication.network.ApiClient;
import com.example.travelexpertmobileapplication.network.api.ProductAPIService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProductDetailsFragment extends Fragment {

    private EditText etProductId, etProductName;
    private Button btnEdit, btnSave;
    private ImageButton btnBack;
    private Product currentProduct;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_product_details, container, false);

        etProductId = view.findViewById(R.id.etProductId);
        etProductName = view.findViewById(R.id.etProductName);
        btnEdit = view.findViewById(R.id.btnEdit);
        btnSave = view.findViewById(R.id.btnSave);
        btnBack = view.findViewById(R.id.btnBack);

        etProductId.setEnabled(false);
        etProductName.setEnabled(false);

        btnBack.setOnClickListener(v -> requireActivity().onBackPressed());

        Bundle args = getArguments();
        if (args != null) {
            int productId = args.getInt("productId");
            String productName = args.getString("productName");

            currentProduct = new Product();
            currentProduct.setProductId(productId);
            currentProduct.setProdname(productName);

            etProductId.setText(String.valueOf(productId));
            etProductName.setText(productName);
        }

        btnEdit.setOnClickListener(v -> etProductName.setEnabled(true));

        btnSave.setOnClickListener(v -> {
            if (currentProduct != null) {
                currentProduct.setProdname(etProductName.getText().toString());

                ProductAPIService api = ApiClient.getClient().create(ProductAPIService.class);
                api.updateProduct(currentProduct.getProductId(), currentProduct).enqueue(new Callback<Product>() {
                    @Override
                    public void onResponse(Call<Product> call, Response<Product> response) {
                        if (response.isSuccessful()) {
                            Toast.makeText(getContext(), "Product updated successfully", Toast.LENGTH_SHORT).show();
                            etProductName.setEnabled(false);
                        } else {
                            Toast.makeText(getContext(), "Update failed: " + response.code(), Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<Product> call, Throwable t) {
                        Toast.makeText(getContext(), "Update error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        return view;
    }
}
