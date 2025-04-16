package com.example.travelexpertmobileapplication.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.travelexpertmobileapplication.R;
import com.example.travelexpertmobileapplication.adapters.PackageAdapter;
import com.example.travelexpertmobileapplication.model.Package;
import com.example.travelexpertmobileapplication.network.ApiClient;
import com.example.travelexpertmobileapplication.network.api.PackageAPIService;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import timber.log.Timber;


public class ModernPackageFragment extends Fragment {

    private RecyclerView recyclerView;

    private PackageAdapter adapter;
    private List<Package> packageList = new ArrayList<>();
    private ImageView btnBack;


    public ModernPackageFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        adapter = new PackageAdapter(packageList, getContext());

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_modern_package, container, false);
        recyclerView = view.findViewById(R.id.package_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new PackageAdapter(packageList, getContext());
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

        fetchPackages();
        return view;
    }


    private void fetchPackages() {
        PackageAPIService apiService = ApiClient.getClient().create(PackageAPIService.class);
        apiService.getPackages().enqueue(new Callback<JsonArray>() {
            @Override
            public void onResponse(@NonNull Call<JsonArray> call, @NonNull Response<JsonArray> response) {
                if (response.isSuccessful() && response.body() != null) {
                    JsonArray array = response.body();
                    packageList.clear();
                    List<Package> packages = new Gson().fromJson(array, new TypeToken<List<Package>>() {
                    }.getType());
                    packageList.clear();
                    packageList.addAll(packages);

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
}