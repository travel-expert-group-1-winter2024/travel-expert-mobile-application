package com.example.travelexpertmobileapplication.fragments;

import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.travelexpertmobileapplication.R;
import com.example.travelexpertmobileapplication.adapters.CustomersAdapter;
import com.example.travelexpertmobileapplication.model.Customer;
import com.example.travelexpertmobileapplication.network.ApiClient;
import com.example.travelexpertmobileapplication.network.api.CustomerAPIService;
import com.example.travelexpertmobileapplication.utils.SharedPrefUtil;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import timber.log.Timber;


public class ModernCustomerFragment extends Fragment {

    private RecyclerView recyclerView;
    private CustomersAdapter adapter;
    private List<Customer> customerList = new ArrayList<>();
    private ImageView btnBack;


    public ModernCustomerFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        adapter = new CustomersAdapter(customerList, getContext());

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_modern_customer, container, false    );
        recyclerView = view.findViewById(R.id.customer_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new CustomersAdapter(customerList, getContext());
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

        fetchCustomers();
        return view;
    }

    private void fetchCustomers() {
        CustomerAPIService apiService = ApiClient.getClient().create(CustomerAPIService.class);

        apiService.getCustomers().enqueue(new Callback<JsonArray>() {
            @Override
            public void onResponse(Call<JsonArray> call, Response<JsonArray> response) {
                if (response.isSuccessful() && response.body() != null) {
                    JsonArray customersArray = response.body();
                    List<Customer> allCustomers = new Gson().fromJson(customersArray, new TypeToken<List<Customer>>() {}.getType());

                    // Parse the JSON array into a list of Customer objects
                    List<Customer> customers = new Gson().fromJson(customersArray, new TypeToken<List<Customer>>(){}.getType());
                    String agentIdStr = SharedPrefUtil.getAgentId(requireContext());
                    List<Customer> filteredCustomers = new ArrayList<>();
                    for (Customer customer : allCustomers) {
                        if (customer.getAgentId() ==  Integer.parseInt(agentIdStr)) {
                            filteredCustomers.add(customer);
                        }
                    }
                    // Clear existing list and add new customer data
                    customerList.clear();
                    customerList.addAll(filteredCustomers);

//                    // Update customer names list for display
//                    customerNames.clear();
//                    for (Customer customer : customersList) {
//                        customerNames.add(customer.getCustfirstname() + " " + customer.getCustlastname());
//                    }

//                    if(customersList.isEmpty()){
//                        customerNames.add("No customers found");
//                    }

                    // Notify adapter of data change
                    adapter.notifyDataSetChanged();
                } else {
                    Timber.e("Response not successful: %s", response.code());
                }
            }

            @Override
            public void onFailure(Call<JsonArray> call, Throwable t) {
                Timber.e(t, "Failed to fetch customers");
            }
        });
    }
}