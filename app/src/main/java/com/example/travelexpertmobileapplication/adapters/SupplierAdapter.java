package com.example.travelexpertmobileapplication.adapters;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.example.travelexpertmobileapplication.R;
import com.example.travelexpertmobileapplication.fragments.SupplierDetailsFragment;
import com.example.travelexpertmobileapplication.model.SupplierContact;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import timber.log.Timber;

public class SupplierAdapter extends RecyclerView.Adapter<SupplierAdapter.MyViewHolder> {
    //* Properties
    private List<SupplierContact> supplierList; //* List of Suppliers
    private Context context; //* Context for inflating views

    //* Constructor
    public SupplierAdapter(List<SupplierContact> supplierList, Context context) {
        this.supplierList = supplierList != null ? supplierList : new ArrayList<>();
        this.context = context;
    }

    //* ViewHolder class to hold views for each item.
    public static class MyViewHolder extends RecyclerView.ViewHolder{
        TextView title, description;

        public MyViewHolder(@NotNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.item_title);
            description = itemView.findViewById(R.id.item_description);
        }
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //* Inflating the card layout for each item
        View view = LayoutInflater.from(context).inflate(R.layout.item_card, parent, false);
        return new MyViewHolder(view); //! Returning a new Viewholder Instance
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        // * Grabbing the data for this position
        SupplierContact data = supplierList.get(position);
//        holder.title.setText(data.getSupconfirstname()); //* Setting title text to be the first name of the contact.
//        holder.description.setText(data.getSupconcompany()); //* Setting the description to be the company name
        holder.title.setText(data.getSupconfirstname() != null ? data.getSupconfirstname() : "No Name");
        holder.description.setText(data.getSupconcompany() != null ? data.getSupconcompany() : "No Company");

        //* Event Handler
        holder.itemView.setOnClickListener(v -> {
            //* Creating a new instance of the detail fragment
            SupplierDetailsFragment supplierDetailsFragment = new SupplierDetailsFragment();

            //* Passing data using bundles
            Bundle bundle = new Bundle();
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.VANILLA_ICE_CREAM) {
//                bundle.putString("item_title", data.getSupconfirstname());
//                bundle.putString("item_description", data.getSupconcompany());
//
//            }

            //* Handle item tap
            bundle.putInt("SupplierContactId", data.getSupplierContactId());
            bundle.putString("FirstName", data.getSupconfirstname());
            bundle.putString("LastName", data.getSupconlastname());
            bundle.putString("Company", data.getSupconcompany());
            bundle.putString("Address", data.getSupconaddress());
            bundle.putString("City", data.getSupconcity());
            bundle.putString("Province", data.getSupconprov());
            bundle.putString("Postal", data.getSupconpostal());
            bundle.putString("Country", data.getSupconcountry());
            bundle.putString("BusinessPhone", data.getSupconbusphone());
            bundle.putString("Fax", data.getSupconfax());
            bundle.putString("Email", data.getSupconemail());
            bundle.putString("Website", data.getSupconurl());
            bundle.putString("AffiliationId", data.getAffiliationid());
            bundle.putInt("SupplierId", data.getSupplierid());

            supplierDetailsFragment.setArguments(bundle);

            FragmentTransaction transaction = ((FragmentActivity)context).getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.fragment_container, supplierDetailsFragment);
            transaction.addToBackStack(null);
            transaction.commit();

        });

    }

    @Override
    public int getItemCount() {
        if (supplierList == null){
            Timber.e("SupplierAdapter: supplierList is null");
            return 0;
        }
        Timber.tag("SupplierAdapter").d("Number of suppliers fetched: " + supplierList.size());

        return supplierList.size(); //* Returning the total number of items.
     }


    /**
     * ! Following comments, for my own understanding, for implementing an adapter.
     * Adapter: The adapter acts as a bridge between the RecyclerView and the data source. It provides the necessary methods to create and bind views for each item in the list.
     * ViewHolder Pattern: The ViewHolder class holds references to the views for each item, which improves performance by avoiding repeated calls to findViewById().
     * onCreateViewHolder: This method inflates the card layout for each item.
     * onBindViewHolder: This method binds the data to the views for each item, setting the title and description.
     * getItemCount: This method returns the total number of items in the data list.
     */




}//!class
