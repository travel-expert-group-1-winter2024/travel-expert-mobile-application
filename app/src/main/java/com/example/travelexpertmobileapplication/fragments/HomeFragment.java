package com.example.travelexpertmobileapplication.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.example.travelexpertmobileapplication.R;
import com.example.travelexpertmobileapplication.model.Product;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public HomeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HomeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        //Finding all the linearLayouts acting as buttons.
        LinearLayout linearLayout_Packages = view.findViewById(R.id.linearLayout_Packages);
        LinearLayout linearLayout_Customers = view.findViewById(R.id.linearyLayout_Customers);
        LinearLayout linearLayout_Suppliers = view.findViewById(R.id.linearyLayout_Suppliers);
        LinearLayout linearLayout_Products = view.findViewById(R.id.linearyLayout_Products);

        //OnClick Handlers for each LinearLayout acting as a button.

        /**
         * LinearLayout acting as Packages "button"
         * OnClick Event Listener to load the Packages Fragment
         */
        linearLayout_Packages.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment packagesFragment = new PackagesFragment();
                FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
                transaction.replace(R.id.fragment_container, packagesFragment);
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });

        /**
         * LinearLayout acting as Customer "button"
         * OnClick Event Listener to load the Customer Fragment
         */
        linearLayout_Customers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment customerFragment = new CustomerFragment();
                FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
                transaction.replace(R.id.fragment_container, customerFragment);
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });

        /**
         * LinearLayout acting as the Suppliers "button"
         * OnClick Event Listener to load the Supplier Fragment
         */
        linearLayout_Suppliers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment supplierFragment = new SupplierFragment();
                FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
                transaction.replace(R.id.fragment_container, supplierFragment);
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });

        /**
         * LinearLayout acting as the Products "button"
         * OnClick Event Listener to load the Products Fragment
         */
        linearLayout_Products.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment productFragment = new ProductFragment();
                FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
                transaction.replace(R.id.fragment_container, productFragment);
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });



        return view;
    }
}