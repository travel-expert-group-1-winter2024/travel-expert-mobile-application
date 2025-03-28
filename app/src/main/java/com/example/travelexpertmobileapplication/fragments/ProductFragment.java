package com.example.travelexpertmobileapplication.fragments;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.travelexpertmobileapplication.R;
import com.example.travelexpertmobileapplication.model.Product;
import com.example.travelexpertmobileapplication.network.ApiClient;
import com.example.travelexpertmobileapplication.utils.ApiEndpoints;
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

    public ProductFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_product, container, false);

        lvProducts = view.findViewById(R.id.lvProducts);
        adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1, productNames);
        lvProducts.setAdapter(adapter);

        fetchProducts();

        return view;
    }

    private void fetchProducts() {
        ApiEndpoints apiService = ApiClient.getClient().create(ApiEndpoints.class);

        apiService.getProducts().enqueue(new Callback<JsonArray>() {
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
}
