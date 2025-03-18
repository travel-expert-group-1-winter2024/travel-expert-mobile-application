package com.example.travelexpertmobileapplication;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class SupplierFragment extends Fragment {

    public SupplierFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_supplier, container, false);

        // UI Elements
        TextView textView = view.findViewById(R.id.textViewSupplier);
        Button btnAddSupplier = view.findViewById(R.id.btnAddSupplier);

        // Button Click Event
        btnAddSupplier.setOnClickListener(v ->
                Toast.makeText(getActivity(), "Add Supplier Clicked", Toast.LENGTH_SHORT).show()
        );

        return view;
    }
}
