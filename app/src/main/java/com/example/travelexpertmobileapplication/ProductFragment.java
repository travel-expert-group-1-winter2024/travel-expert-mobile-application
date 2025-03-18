package com.example.travelexpertmobileapplication;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class ProductFragment extends Fragment {

    public ProductFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_product, container, false);

        // UI Elements
        TextView textView = view.findViewById(R.id.textViewProduct);
        Button btnAddProduct = view.findViewById(R.id.btnAddProduct);

        // Button Click Event
        btnAddProduct.setOnClickListener(v ->
                Toast.makeText(getActivity(), "Add Product Clicked", Toast.LENGTH_SHORT).show()
        );

        return view;
    }
}
