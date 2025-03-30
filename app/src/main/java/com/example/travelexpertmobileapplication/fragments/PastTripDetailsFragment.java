package com.example.travelexpertmobileapplication.fragments;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;

import com.example.travelexpertmobileapplication.R;

public class PastTripDetailsFragment extends Fragment {
    EditText etSupplier, etProduct, etFee, etcustomerid, etClass, etRegion, etlastname, etfirstname,
            etTripstartdate,etTripenddate,etDescription,etagentid,etagencyCommsion,etBaseprice,etDestination,etiternaryNo;
    ImageButton btnBack2;

    public PastTripDetailsFragment() {
        // Required empty public constructor
    }

    public static PastTripDetailsFragment newInstance(String param1, String param2) {
        PastTripDetailsFragment fragment = new PastTripDetailsFragment();

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_past_trip_details, container, false);

        // Initialize EditText fields
        EditText etSupplier = view.findViewById(R.id.etSupplier);
        EditText etProduct = view.findViewById(R.id.etProduct);
        EditText etFee = view.findViewById(R.id.etFee);
        EditText etCustomerId = view.findViewById(R.id.etcustomerid);
        EditText etClass = view.findViewById(R.id.etClass);
        EditText etRegion = view.findViewById(R.id.etRegion);
        EditText etLastName = view.findViewById(R.id.etlastname);
        EditText etFirstName = view.findViewById(R.id.etfirstname);
        EditText etTripStartDate = view.findViewById(R.id.etTripstartdate);
        EditText etTripEndDate = view.findViewById(R.id.etTripenddate);
        EditText etDescription = view.findViewById(R.id.etDescription);
        EditText etAgentId = view.findViewById(R.id.etagentid);
        EditText etAgencyCommission = view.findViewById(R.id.etagencyCommsion);
        EditText etBasePrice = view.findViewById(R.id.etBaseprice);
        EditText etDestination = view.findViewById(R.id.etDestination);
        EditText etItineraryNo = view.findViewById(R.id.etiternaryNo);
        ImageButton btnBack2 = view.findViewById(R.id.btnBack2);

        btnBack2.setOnClickListener(v -> {
            if (getActivity() != null) {
                getActivity().onBackPressed(); // Go back to the previous screen
            }
        });

        // Get Data from Bundle
        Bundle bundle = getArguments();
        if (bundle != null) {
            etSupplier.setText(bundle.getString("supplier"));
            etProduct.setText(bundle.getString("product"));
            etFee.setText(bundle.getString("fee"));
            etCustomerId.setText(String.valueOf(bundle.getInt("customerid")));
            etClass.setText(bundle.getString("className"));
            etRegion.setText(bundle.getString("region"));
            etLastName.setText(bundle.getString("lastname"));
            etFirstName.setText(bundle.getString("firstname"));
            etTripStartDate.setText(bundle.getString("tripStart"));
            etTripEndDate.setText(bundle.getString("tripEnd"));
            etDescription.setText(bundle.getString("description"));
            etAgentId.setText(String.valueOf(bundle.getInt("agentid")));
            etAgencyCommission.setText(String.valueOf(bundle.getDouble("agencyCommission")));
            etBasePrice.setText(String.valueOf(bundle.getDouble("basePrice")));
            etDestination.setText(bundle.getString("destination"));
            etItineraryNo.setText(String.valueOf(bundle.getInt("itineraryNo")));
        }

//        etAgencyCommission.setEnabled(false);
//        etBasePrice.setEnabled(false);
//        etDestination.setEnabled(false);
//        etItineraryNo.setEnabled(false);
//        etTripStartDate.setEnabled(false);
//        etTripEndDate.setEnabled(false);
//        etDescription.setEnabled(false);
//        etAgentId.setEnabled(false);
//        etcustomerid.setEnabled(false);
//        etClass.setEnabled(false);
//        etRegion.setEnabled(false);
//        etLastName.setEnabled(false);
//        etFirstName.setEnabled(false);
//        etSupplier.setEnabled(false);
//        etProduct.setEnabled(false);
//        etFee.setEnabled(false);

        return view;
    }
}