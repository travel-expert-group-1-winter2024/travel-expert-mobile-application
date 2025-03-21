package com.example.travelexpertmobileapplication;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.travelexpertmobileapplication.Models.Customer;
import com.example.travelexpertmobileapplication.utils.ApiClient;
import com.example.travelexpertmobileapplication.utils.ApiEndpoints;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class CustomerDetailsFragment extends Fragment {

    EditText etAgentID, etEmail, etHomePhone, etBusinessPhone, etCountry, etPostalCode,etCustomerID, etProvince, etCity, etAddress, etFirstName, etLastName;
    Button saveBtn, editBtn;

    public CustomerDetailsFragment() {
        // Required empty public constructor
    }

    public static CustomerDetailsFragment newInstance(String param1, String param2) {
        CustomerDetailsFragment fragment = new CustomerDetailsFragment();
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
        return inflater.inflate(R.layout.fragment_customer_details, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // Initialize EditText fields
        etAgentID = view.findViewById(R.id.etAgentID);
        etEmail = view.findViewById(R.id.etEmail);
        etHomePhone = view.findViewById(R.id.etHomePhone);
        etBusinessPhone = view.findViewById(R.id.etBusinessPhone);
        etCountry = view.findViewById(R.id.etCountry);
        etPostalCode = view.findViewById(R.id.etPostalCode);
        etCustomerID = view.findViewById(R.id.etCustomerID);
        etProvince = view.findViewById(R.id.etProvince);
        etCity = view.findViewById(R.id.etCity);
        etAddress = view.findViewById(R.id.etAddress);
        etFirstName = view.findViewById(R.id.etFirstName);
        etLastName = view.findViewById(R.id.etLastName);

        // Initialize Buttons
        saveBtn = view.findViewById(R.id.saveBtn);
        editBtn = view.findViewById(R.id.editBtn);

        // Set default data (You can retrieve from arguments or a database)
        Bundle args = getArguments();
        if (args != null) {
            etAgentID.setText(args.getString("AgentID", ""  ));
            etEmail.setText(args.getString("Email", ""));
            etHomePhone.setText(args.getString("HomePhone", ""));
            etBusinessPhone.setText(args.getString("BusinessPhone", ""));
            etCountry.setText(args.getString("Country", ""));
            etPostalCode.setText(args.getString("PostalCode", ""));
            etCustomerID.setText(args.getString("CustomerID", ""));
            etProvince.setText(args.getString("Province", ""));
            etCity.setText(args.getString("City", ""));
            etAddress.setText(args.getString("Address", ""));
            etFirstName.setText(args.getString("FirstName", ""));
            etLastName.setText(args.getString("LastName", ""));
        }

        // Set up button click listeners (Example)
        editBtn.setOnClickListener(v -> {
            // Enable editing
            etFirstName.setEnabled(true);
            etLastName.setEnabled(true);
            etEmail.setEnabled(true);
            etHomePhone.setEnabled(true);
            etBusinessPhone.setEnabled(true);
            etCountry.setEnabled(true);
            etPostalCode.setEnabled(true);
            etCustomerID.setEnabled(true);
            etProvince.setEnabled(true);
            etCity.setEnabled(true);
            etAddress.setEnabled(true);
            etAgentID.setEnabled(true);
        });

        saveBtn.setOnClickListener(v -> {
            // Close the fragment
//            requireActivity().getSupportFragmentManager().popBackStack();
            updateCustomer();
        });
    }

    private void updateCustomer() {
        // Create a CustomerDTO object
        Customer updatedCustomerDTO = new Customer();
        int customerID = Integer.parseInt(etCustomerID.getText().toString());
        updatedCustomerDTO.setCustfirstname(etFirstName.getText().toString());
        updatedCustomerDTO.setCustlastname(etLastName.getText().toString());
        updatedCustomerDTO.setCustaddress(etAddress.getText().toString());
        updatedCustomerDTO.setCustcity(etCity.getText().toString());
        updatedCustomerDTO.setCustprov(etProvince.getText().toString());
        updatedCustomerDTO.setCustpostal(etPostalCode.getText().toString());
        updatedCustomerDTO.setCustcountry(etCountry.getText().toString());
        updatedCustomerDTO.setCusthomephone(etHomePhone.getText().toString());
        updatedCustomerDTO.setCustbusphone(etBusinessPhone.getText().toString());
        updatedCustomerDTO.setCustemail(etEmail.getText().toString());
        updatedCustomerDTO.setAgentId(Integer.parseInt(String.valueOf(etAgentID.getText())));
        updatedCustomerDTO.setCustomerid(customerID);
        // Create Retrofit instance
        ApiEndpoints apiService = ApiClient.getClient().create(ApiEndpoints.class);

        // Make the PUT request to update the customer
        Call<Customer> call = apiService.updateCustomer(customerID, updatedCustomerDTO);
        call.enqueue(new Callback<Customer>() {
            @Override
            public void onResponse(Call<Customer> call, Response<Customer> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(getActivity(), "Customer updated successfully", Toast.LENGTH_SHORT).show();
                    requireActivity().getSupportFragmentManager().popBackStack();
                } else {
                    Toast.makeText(getActivity(), "Failed to update customer", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Customer> call, Throwable t) {
                Toast.makeText(getActivity(), "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });
    }
}
