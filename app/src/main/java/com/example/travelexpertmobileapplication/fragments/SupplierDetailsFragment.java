package com.example.travelexpertmobileapplication.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.travelexpertmobileapplication.R;
import com.example.travelexpertmobileapplication.model.SupplierContact;
import com.example.travelexpertmobileapplication.network.ApiClient;
import com.example.travelexpertmobileapplication.utils.ApiEndpoints;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SupplierDetailsFragment extends Fragment {

    private EditText etSupplierContactId, etFirstName, etLastName, etCompany, etAddress,
            etCity, etProvince, etPostal, etCountry, etBusinessPhone, etFax,
            etEmail, etWebsite, etAffiliationId, etSupplierId;
    private Button btnEdit, btnSave;
    private boolean isEditable = false;
    private ApiEndpoints api;
    private int supplierContactId;

    public SupplierDetailsFragment() {}

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_supplier_details, container, false);
        api = ApiClient.getClient().create(ApiEndpoints.class);

        // 🔹 Initialize Views
        etSupplierContactId = view.findViewById(R.id.etSupplierContactId);
        etFirstName = view.findViewById(R.id.etFirstName);
        etLastName = view.findViewById(R.id.etLastName);
        etCompany = view.findViewById(R.id.etCompany);
        etAddress = view.findViewById(R.id.etAddress);
        etCity = view.findViewById(R.id.etCity);
        etProvince = view.findViewById(R.id.etProvince);
        etPostal = view.findViewById(R.id.etPostal);
        etCountry = view.findViewById(R.id.etCountry);
        etBusinessPhone = view.findViewById(R.id.etBusinessPhone);
        etFax = view.findViewById(R.id.etFax);
        etEmail = view.findViewById(R.id.etEmail);
        etWebsite = view.findViewById(R.id.etWebsite);
        etAffiliationId = view.findViewById(R.id.etAffiliationId);
        etSupplierId = view.findViewById(R.id.etSupplierId);
        btnEdit = view.findViewById(R.id.btnEdit);
        btnSave = view.findViewById(R.id.btnSave);

        // 🔹 Get data from arguments
        Bundle args = getArguments();
        if (args != null) {
            supplierContactId = args.getInt("SupplierContactId");
            etSupplierContactId.setText(String.valueOf(supplierContactId));
            etFirstName.setText(args.getString("FirstName"));
            etLastName.setText(args.getString("LastName"));
            etCompany.setText(args.getString("Company"));
            etAddress.setText(args.getString("Address"));
            etCity.setText(args.getString("City"));
            etProvince.setText(args.getString("Province"));
            etPostal.setText(args.getString("Postal"));
            etCountry.setText(args.getString("Country"));
            etBusinessPhone.setText(args.getString("BusinessPhone"));
            etFax.setText(args.getString("Fax"));
            etEmail.setText(args.getString("Email"));
            etWebsite.setText(args.getString("Website"));
            etAffiliationId.setText(args.getString("AffiliationId"));
            etSupplierId.setText(String.valueOf(args.getInt("SupplierId")));
        }

        // 🔹 Disable inputs initially
        setEditable(false);

        // 🔹 Set button listeners
        btnEdit.setOnClickListener(v -> setEditable(true));
        btnSave.setOnClickListener(v -> saveSupplier());

        return view;
    }

    private void setEditable(boolean enabled) {
        etFirstName.setEnabled(enabled);
        etLastName.setEnabled(enabled);
        etCompany.setEnabled(enabled);
        etAddress.setEnabled(enabled);
        etCity.setEnabled(enabled);
        etProvince.setEnabled(enabled);
        etPostal.setEnabled(enabled);
        etCountry.setEnabled(enabled);
        etBusinessPhone.setEnabled(enabled);
        etFax.setEnabled(enabled);
        etEmail.setEnabled(enabled);
        etWebsite.setEnabled(enabled);
        etAffiliationId.setEnabled(enabled);
        etSupplierId.setEnabled(enabled);
        btnSave.setEnabled(enabled);
        isEditable = enabled;
    }

    private void saveSupplier() {
        SupplierContact updated = new SupplierContact();
        updated.setSupplierContactId(supplierContactId);
        updated.setSupconfirstname(etFirstName.getText().toString());
        updated.setSupconlastname(etLastName.getText().toString());
        updated.setSupconcompany(etCompany.getText().toString());
        updated.setSupconaddress(etAddress.getText().toString());
        updated.setSupconcity(etCity.getText().toString());
        updated.setSupconprov(etProvince.getText().toString());
        updated.setSupconpostal(etPostal.getText().toString());
        updated.setSupconcountry(etCountry.getText().toString());
        updated.setSupconbusphone(etBusinessPhone.getText().toString());
        updated.setSupconfax(etFax.getText().toString());
        updated.setSupconemail(etEmail.getText().toString());
        updated.setSupconurl(etWebsite.getText().toString());
        updated.setAffiliationid(etAffiliationId.getText().toString());
        updated.setSupplierid(Integer.parseInt(etSupplierId.getText().toString()));

        api.updateSupplierContact(supplierContactId, updated).enqueue(new Callback<SupplierContact>() {
            @Override
            public void onResponse(Call<SupplierContact> call, Response<SupplierContact> response) {
                if (response.isSuccessful()) {
                    Log.i("Supplier", "Updated successfully");
                    setEditable(false);
                } else {
                    Log.e("Supplier", "Update failed: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<SupplierContact> call, Throwable t) {
                Log.e("Supplier", "Error: " + t.getMessage());
            }
        });
    }
}
