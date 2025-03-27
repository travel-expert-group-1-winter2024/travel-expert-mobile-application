package com.example.travelexpertmobileapplication.fragments;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import com.example.travelexpertmobileapplication.R;
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

public class PackagesFragment extends Fragment {

    ListView lvPackages;
    ArrayAdapter<String> adapter;
    List<String> packageNames = new ArrayList<>();
    List<Package> packagesList = new ArrayList<>();

    public PackagesFragment() {
        // Required empty public constructor
    }

    public static PackagesFragment newInstance() {
        return new PackagesFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_packages, container, false);

        // Initialize views
        lvPackages = view.findViewById(R.id.lvPackages);
        adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1, packageNames);
        lvPackages.setAdapter(adapter);

        // Fetch packages from the backend
        fetchPackages();

        // Set click listener for package items
        lvPackages.setOnItemClickListener((parent, view1, position, id) -> openPackageDetailsFragment(position));

        // Initialize the "Add New Package" button
        Button btnAddPackage = view.findViewById(R.id.btnAddPackage);
        btnAddPackage.setOnClickListener(v -> {
            // Open PackageDetailsFragment in add mode
            PackageDetailsFragment fragment = new PackageDetailsFragment();
            FragmentTransaction transaction = requireActivity().getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.fragment_container, fragment); // Replace with the container ID in your activity
            transaction.addToBackStack(null);
            transaction.commit();
        });

        return view;
    }

    private void fetchPackages() {
        PackageAPIService apiService = ApiClient.getClient().create(PackageAPIService.class);

        apiService.getPackages().enqueue(new Callback<JsonArray>() {
            @Override
            public void onResponse(Call<JsonArray> call, Response<JsonArray> response) {
                if (response.isSuccessful() && response.body() != null) {
                    JsonArray packagesArray = response.body();  // Response is an array of packages

                    // Parse the JSON array into a list of Package objects
                    List<Package> packages = new Gson().fromJson(packagesArray, new TypeToken<List<Package>>(){}.getType());

                    // Clear existing list and add new package data
                    packagesList.clear();
                    packagesList.addAll(packages);
                    Log.d("Api packages", packages.toString());
                    // Update package names list for display
                    packageNames.clear();
                    for (Package pkg : packagesList) {
                        packageNames.add(pkg.getPkgname() + " - " + pkg.getPkgdesc());
                    }

                    // Notify adapter of data change
                    adapter.notifyDataSetChanged();
                } else {
                    Timber.e("Response not successful: %s", response.code());
                }
            }

            @Override
            public void onFailure(Call<JsonArray> call, Throwable t) {
                Timber.e("Failed to fetch packages: %s", t.getMessage());
            }
        });
    }

    private void openPackageDetailsFragment(int position) {
        if (position < 0 || position >= packagesList.size()) return;

        Package selectedPackage = packagesList.get(position);
        Timber.i("Selected Package: %s", selectedPackage.toString());
        // Create a Bundle to pass data
        Bundle bundle = new Bundle();
//        bundle.putInt("PackageID", selectedPackage.getPackageid());
        bundle.putSerializable("PackageDetails", selectedPackage);

        // Open PackageDetailsFragment and pass data
        PackageDetailsFragment fragment = new PackageDetailsFragment();
        fragment.setArguments(bundle);

        FragmentTransaction transaction = requireActivity().getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, fragment); // Replace with the container ID in your activity
        transaction.addToBackStack(null);
        transaction.commit();
    }
}