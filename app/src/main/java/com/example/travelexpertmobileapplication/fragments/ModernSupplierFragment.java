package com.example.travelexpertmobileapplication.fragments;

import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.travelexpertmobileapplication.R;
import com.example.travelexpertmobileapplication.SupplierAdapter;
import com.example.travelexpertmobileapplication.model.SupplierContact;
import com.example.travelexpertmobileapplication.network.ApiClient;
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

public class ModernSupplierFragment extends Fragment {
    private RecyclerView recyclerView;
    private SupplierAdapter adapter;
    private List<SupplierContact> supplierList = new ArrayList<>();

    private ImageView btnBack;



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        adapter = new SupplierAdapter(supplierList, getContext());


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_modern_supplier, container, false);
        recyclerView = view.findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new SupplierAdapter(supplierList, getContext());
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

        fetchSuppliers();
        return view;
    }

    private void fetchSuppliers() {
        SupplierContactAPIService apiService = ApiClient.getClient().create(SupplierContactAPIService.class);
        apiService.getSupplierContacts().enqueue(new Callback<JsonArray>() {
            @Override
            public void onResponse(@NonNull Call<JsonArray> call, @NonNull Response<JsonArray> response) {
                if (response.isSuccessful() && response.body() != null) {
                    JsonArray array = response.body();
                    supplierList.clear();
                    List<SupplierContact> contacts = new Gson().fromJson(array, new TypeToken<List<SupplierContact>>() {}.getType());
                    List<SupplierContact> dummyData = new ArrayList<>();
                    for (int i = 0; i < 10; i++) {
                        SupplierContact contact = new SupplierContact();
                        contact.setSupconfirstname("First " + i);
                        contact.setSupconcompany("Company " + i);
                        dummyData.add(contact);
                    }
                    //supplierList.addAll(dummyData);
                    supplierList.clear();
                    supplierList.addAll(contacts);

                    //Timber.tag("SupplierAdapter").d("Number of suppliers fetched: " + supplierList.size()); // !Debugging
                    for (SupplierContact contact : contacts){
                        //Timber.tag("SupplierAdapter").d("Supplier: " + contact.getSupconfirstname() + ", Company: " + contact.getSupconcompany()); //! Debugging
                    }

                    adapter.notifyDataSetChanged();
                } else {
                    Timber.tag("API").e("Error loading supplier contacts");
                }
            }

            @Override
            public void onFailure(Call<JsonArray> call, Throwable t) {
                Timber.tag("API").e("Failure: %s", t.getMessage());
            }
        });
    }



}//! Class