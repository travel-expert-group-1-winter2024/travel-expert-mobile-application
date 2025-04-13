package com.example.travelexpertmobileapplication.adapters;

import android.content.Context;
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
import com.example.travelexpertmobileapplication.fragments.CustomerDetailsFragment;
import com.example.travelexpertmobileapplication.model.Customer;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import timber.log.Timber;

public class CustomersAdapter extends RecyclerView.Adapter<CustomersAdapter.MyViewHolder> {

    private List<Customer> customerList;
    private Context context;

    //! Constructor
    public CustomersAdapter(List<Customer> customerList, Context context) {
        this.customerList = customerList != null ? customerList : new ArrayList<>();
        this.context = context;
    }

    @NonNull
    @Override
    public CustomersAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_card, parent, false);
        return new MyViewHolder(view); //! Returning a new Viewholder Instance
    }

    @Override
    public void onBindViewHolder(@NonNull CustomersAdapter.MyViewHolder holder, int position) {
        Customer data = customerList.get(position);

        String customerFirstLastName = String.format(data.getCustfirstname() + " " + data.getCustlastname());
        String customerContactInfo = String.format(data.getCustemail() + " " + data.getCusthomephone());

        holder.title.setText(customerFirstLastName != null ? customerFirstLastName : "No Customer Name on record");
        holder.description.setText(customerContactInfo != null ? customerContactInfo : "No Contact Info on record");

        holder.itemView.setOnClickListener(v -> {
            CustomerDetailsFragment customerDetailsFragment = new CustomerDetailsFragment();

            //* Passing data through bundles

            Bundle bundle = new Bundle();
            bundle.putString("AgentID", String.valueOf(data.getAgentId()));
            bundle.putString("Email", data.getCustemail());
            bundle.putString("HomePhone", data.getCusthomephone());
            bundle.putString("BusinessPhone", data.getCustbusphone());
            bundle.putString("Country", data.getCustcountry());
            bundle.putString("PostalCode", data.getCustpostal());
            bundle.putString("CustomerID", String.valueOf(data.getCustomerid()));
            bundle.putString("Province", data.getCustprov());
            bundle.putString("City", data.getCustcity());
            bundle.putString("Address", data.getCustaddress());
            bundle.putString("FirstName", data.getCustfirstname());
            bundle.putString("LastName", data.getCustlastname());

            //* Fragment Transaction
            customerDetailsFragment.setArguments(bundle);

            FragmentTransaction transaction = ((FragmentActivity)context).getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.fragment_container, customerDetailsFragment);
            transaction.addToBackStack(null);
            transaction.commit();

        });
    }

    @Override
    public int getItemCount() {
        if (customerList == null){
            Timber.e("SupplierAdapter: supplierList is null");
            return 0;
        }
        Timber.tag("Customer Adapter").d("Number of customers fetched: " + customerList.size());

        return customerList.size(); //* Returning the total number of items.
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{

        TextView title, description;

        public MyViewHolder(@NotNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.item_title);
            description = itemView.findViewById(R.id.item_description);
        }



    }


}//! Class
