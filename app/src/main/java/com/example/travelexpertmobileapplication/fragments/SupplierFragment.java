package com.example.travelexpertmobileapplication.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.travelexpertmobileapplication.R;
import com.example.travelexpertmobileapplication.model.SupplierContact;
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

public class SupplierFragment extends Fragment {

    private ListView lvSuppliers;
    private ArrayAdapter<String> adapter;
    private List<String> supplierNames = new ArrayList<>();
    private List<SupplierContact> supplierList = new ArrayList<>();

    public SupplierFragment() {}

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_supplier, container, false);

        lvSuppliers = view.findViewById(R.id.lvSuppliers);
        adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1, supplierNames);
        lvSuppliers.setAdapter(adapter);

        fetchSuppliers();

        lvSuppliers.setOnItemClickListener((parent, view1, position, id) -> openSupplierDetails(position));

        return view;
    }

    private void fetchSuppliers() {
        ApiEndpoints apiService = ApiClient.getClient().create(ApiEndpoints.class);
        apiService.getSupplierContacts().enqueue(new Callback<JsonArray>() {
            @Override
            public void onResponse(Call<JsonArray> call, Response<JsonArray> response) {
                if (response.isSuccessful() && response.body() != null) {
                    JsonArray array = response.body();
                    supplierList.clear();
                    supplierList.addAll(new Gson().fromJson(array, new TypeToken<List<SupplierContact>>() {}.getType()));

                    supplierNames.clear();
                    for (SupplierContact sc : supplierList) {
                        supplierNames.add(sc.getSupconfirstname() + " " + sc.getSupconlastname());
                    }
                    adapter.notifyDataSetChanged();
                } else {
                    Log.e("API", "Error loading supplier contacts");
                }
            }

            @Override
            public void onFailure(Call<JsonArray> call, Throwable t) {
                Log.e("API", "Failure: " + t.getMessage());
            }
        });
    }

    private void openSupplierDetails(int position) {
        if (position < 0 || position >= supplierList.size()) return;

        SupplierContact selected = supplierList.get(position);
        Bundle bundle = new Bundle();
        bundle.putInt("SupplierContactId", selected.getSupplierContactId());
        bundle.putString("FirstName", selected.getSupconfirstname());
        bundle.putString("LastName", selected.getSupconlastname());
        bundle.putString("Company", selected.getSupconcompany());
        bundle.putString("Address", selected.getSupconaddress());
        bundle.putString("City", selected.getSupconcity());
        bundle.putString("Province", selected.getSupconprov());
        bundle.putString("Postal", selected.getSupconpostal());
        bundle.putString("Country", selected.getSupconcountry());
        bundle.putString("BusinessPhone", selected.getSupconbusphone());
        bundle.putString("Fax", selected.getSupconfax());
        bundle.putString("Email", selected.getSupconemail());
        bundle.putString("Website", selected.getSupconurl());
        bundle.putString("AffiliationId", selected.getAffiliationid());
        bundle.putInt("SupplierId", selected.getSupplierid());

        SupplierDetailsFragment detailsFragment = new SupplierDetailsFragment();
        detailsFragment.setArguments(bundle);

        FragmentTransaction transaction = requireActivity().getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, detailsFragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }
}
