package com.example.travelexpertmobileapplication.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;

import com.example.travelexpertmobileapplication.model.Customer;
import com.example.travelexpertmobileapplication.R;
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

public class CustomerFragment extends Fragment {

    ListView lvCustomers;
    ArrayAdapter<String> adapter;
    List<String> customerNames = new ArrayList<>();
    List<Customer> customersList = new ArrayList<>();
    ImageButton btnBack3;

    public CustomerFragment() {
        // Required empty public constructor
    }

    public static CustomerFragment newInstance(String param1, String param2) {
        CustomerFragment fragment = new CustomerFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_customer, container, false);
        lvCustomers = view.findViewById(R.id.lvCustomers);
        btnBack3 = view.findViewById(R.id.btnBack3);
        btnBack3.setOnClickListener(v -> requireActivity().getSupportFragmentManager().popBackStack());
        adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1, customerNames);
        lvCustomers.setAdapter(adapter);
        fetchCustomers();
        lvCustomers.setOnItemClickListener((parent, view1, position, id) -> openCustomerDetailsFragment(position));
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
                    customersList.clear();
                    customersList.addAll(filteredCustomers);

                    // Update customer names list for display
                    customerNames.clear();
                    for (Customer customer : customersList) {
                        customerNames.add(customer.getCustfirstname() + " " + customer.getCustlastname());
                    }

                    if(customersList.isEmpty()){
                        customerNames.add("No customers found");
                    }

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

    private void openCustomerDetailsFragment(int position) {
        if (position < 0 || position >= customersList.size()) return;

        Customer selectedCustomer = customersList.get(position);

        // Create a Bundle to pass data
        Bundle bundle = new Bundle();
        bundle.putString("AgentID", String.valueOf(selectedCustomer.getAgentId()));
        bundle.putString("Email", selectedCustomer.getCustemail());
        bundle.putString("HomePhone", selectedCustomer.getCusthomephone());
        bundle.putString("BusinessPhone", selectedCustomer.getCustbusphone());
        bundle.putString("Country", selectedCustomer.getCustcountry());
        bundle.putString("PostalCode", selectedCustomer.getCustpostal());
        bundle.putString("CustomerID", String.valueOf(selectedCustomer.getCustomerid()));
        bundle.putString("Province", selectedCustomer.getCustprov());
        bundle.putString("City", selectedCustomer.getCustcity());
        bundle.putString("Address", selectedCustomer.getCustaddress());
        bundle.putString("FirstName", selectedCustomer.getCustfirstname());
        bundle.putString("LastName", selectedCustomer.getCustlastname());

        // Open CustomerDetailsFragment and pass data
        CustomerDetailsFragment fragment = new CustomerDetailsFragment();
        fragment.setArguments(bundle);

        FragmentTransaction transaction = requireActivity().getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, fragment); // Replace with the container ID in your activity
        transaction.addToBackStack(null);
        transaction.commit();
    }
}