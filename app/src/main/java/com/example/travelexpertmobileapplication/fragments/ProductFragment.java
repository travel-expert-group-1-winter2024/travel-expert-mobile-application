package com.example.travelexpertmobileapplication.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.travelexpertmobileapplication.R;
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

public class ProductFragment extends Fragment {

    ListView lvProducts;
    ArrayAdapter<String> adapter;
    List<Product> productList = new ArrayList<>();
    List<String> productNames = new ArrayList<>();

    public ProductFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_product, container, false);

        lvProducts = view.findViewById(R.id.lvProducts);
        adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1, productNames);
        lvProducts.setAdapter(adapter);

        // ✅ دکمه Back
        ImageButton btnBack = view.findViewById(R.id.btnBack);
        btnBack.setOnClickListener(v -> requireActivity().onBackPressed());

        lvProducts.setOnItemClickListener((parent, view1, position, id) -> {
            Product selectedProduct = productList.get(position);
            showProductDetails(selectedProduct);
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
                    List<Product> products = new Gson().fromJson(response.body(), new TypeToken<List<Product>>() {}.getType());
                    productList.clear();
                    productList.addAll(products);
                    productNames.clear();

                    for (Product product : productList) {
                        productNames.add(product.getProdname());
                    }

                    adapter.notifyDataSetChanged();
                } else {
                    Log.e("API Error", "Product response not successful: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<JsonArray> call, Throwable t) {
                Log.e("API Error", "Failed to fetch products: " + t.getMessage());
            }
        });
    }

    private void showProductDetails(Product product) {
        ProductDetailsFragment detailsFragment = new ProductDetailsFragment();

        Bundle args = new Bundle();
        args.putInt("productId", product.getProductId());
        args.putString("productName", product.getProdname());
        detailsFragment.setArguments(args);

        FragmentTransaction transaction = requireActivity().getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, detailsFragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }
}
