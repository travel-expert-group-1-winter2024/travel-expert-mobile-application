package com.example.travelexpertmobileapplication.fragments;

import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.travelexpertmobileapplication.R;
import com.example.travelexpertmobileapplication.adapters.ProductsAdapter;
import com.example.travelexpertmobileapplication.adapters.SupplierAdapter;
import com.example.travelexpertmobileapplication.model.Product;
import com.example.travelexpertmobileapplication.network.ApiClient;
import com.example.travelexpertmobileapplication.network.api.ProductAPIService;
import com.example.travelexpertmobileapplication.network.api.SupplierContactAPIService;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import timber.log.Timber;

public class ModernProductsFragment extends Fragment {

    private RecyclerView recyclerView;
    private ProductsAdapter adapter;
    private List<Product> productList = new ArrayList<>();

    private ImageView btnBack;


    public ModernProductsFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        adapter = new ProductsAdapter(productList, getContext());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_modern_products, container, false);
        recyclerView = view.findViewById(R.id.product_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new ProductsAdapter(productList, getContext());
        recyclerView.setAdapter(adapter);
        btnBack = view.findViewById(R.id.btnBack);

        // Register the callback
        requireActivity().getOnBackPressedDispatcher().addCallback(getViewLifecycleOwner(), new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                // Handle the back press here
                // For example, navigate back or perform any action
                requireActivity().getSupportFragmentManager().popBackStack();
            }
        });

        // Set up your button click listener
        btnBack.setOnClickListener(v -> {
            // Optionally, you can also call the back press handler directly
            requireActivity().getOnBackPressedDispatcher().onBackPressed();
        });

        fetchProducts();
        return view;
    }

    private void fetchProducts() {
        SupplierContactAPIService supplierContactAPIService = ApiClient.getClient().create(SupplierContactAPIService.class);
        ProductAPIService productAPIService = ApiClient.getClient().create(ProductAPIService.class);
        productAPIService.getProducts().enqueue(new Callback<JsonArray>() {
            @Override
            public void onResponse(Call<JsonArray> call, Response<JsonArray> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Timber.tag("API Response").e("Raw JSON: %s", response.body().toString()); //!Debugging


                    List<Product> products = new Gson().fromJson(response.body(), new TypeToken<List<Product>>() {
                    }.getType());
                    productList.clear();
                    productList.addAll(products);
//                    for (Product product : productList) {
//                        Timber.tag("Data Fetch").e("Fetched Product: %s", product.toString()); //! Debugging
//
//                    }


                    adapter.notifyDataSetChanged();
                } else {
                    Timber.tag("API Error").e("Product response not successful: %s", response.code());
                }
            }

            @Override
            public void onFailure(Call<JsonArray> call, Throwable t) {
                Timber.tag("API Error").e("Failed to fetch products: %s", t.getMessage());
            }
        });
    }

}//! Class