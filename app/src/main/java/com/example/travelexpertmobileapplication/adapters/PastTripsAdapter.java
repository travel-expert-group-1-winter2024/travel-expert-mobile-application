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
import com.example.travelexpertmobileapplication.fragments.PastTripDetailsFragment;
import com.example.travelexpertmobileapplication.model.PastTripsModel;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import timber.log.Timber;

public class PastTripsAdapter extends RecyclerView.Adapter<PastTripsAdapter.MyViewHolder> {

    private List<PastTripsModel> pastTripsModelList;
    private Context context;

    public PastTripsAdapter(List<PastTripsModel> pastTripsModelList, Context context) {
        this.pastTripsModelList = pastTripsModelList != null ? pastTripsModelList : new ArrayList<>();
        this.context = context;
    }

    @NonNull
    @Override
    public PastTripsAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_card, parent, false);
        return new MyViewHolder(view); //! Returning a new Viewholder Instance
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        PastTripsModel data = pastTripsModelList.get(position);

        holder.title.setText(data.getCustomerName() != null ? data.getCustomerName() : "No Customer name on record");
        holder.description.setText(data.getDestination() != null ? data.getDestination() : "No Destination on record");

        holder.itemView.setOnClickListener(v -> {
            PastTripDetailsFragment pastTripDetailsFragment = new PastTripDetailsFragment();

            Bundle bundle = new Bundle();

            bundle.putInt("bookingDetailId", data.getBookingDetailId());
            bundle.putInt("itineraryNo", data.getItineraryNo());
            bundle.putString("tripStart", data.getTripStart() != null ? data.getTripStart() : "");
            bundle.putString("tripEnd", data.getTripEnd() != null ? data.getTripEnd() : "");
            bundle.putString("description", data.getDescription() != null ? data.getDescription() : "");
            bundle.putString("destination", data.getDestination() != null ? data.getDestination() : "");
            bundle.putDouble("basePrice", data.getBasePrice() != 0 ? data.getBasePrice() : 0.0);
            bundle.putDouble("agencyCommission", data.getAgencyCommission() != 0 ? data.getAgencyCommission() : 0.0);
            bundle.putInt("agentid", data.getAgentId());
            bundle.putInt("customerid", data.getCustomerId());
            bundle.putString("firstname", data.getFirstName() != null ? data.getFirstName() : "");
            bundle.putString("lastname", data.getLastName() != null ? data.getLastName() : "");
            bundle.putString("region", data.getRegion() != null ? data.getRegion() : "");
            bundle.putString("className", data.getClassName() != null ? data.getClassName() : "");
            bundle.putString("fee", data.getFee() != null ? data.getFee() : "");
            bundle.putString("product", data.getProduct() != null ? data.getProduct() : "");
            bundle.putString("supplier", data.getSupplier() != null ? data.getSupplier() : "");

            pastTripDetailsFragment.setArguments(bundle);

            FragmentTransaction transaction = ((FragmentActivity)context).getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.fragment_container, pastTripDetailsFragment);
            transaction.addToBackStack(null);
            transaction.commit();
        });
    }

    @Override
    public int getItemCount() {
        if (pastTripsModelList == null){
            Timber.e("SupplierAdapter: supplierList is null");
            return 0;
        }
        Timber.tag("PastTrips Adapter").d("Number of Past Trips fetched: " + pastTripsModelList.size());

        return pastTripsModelList.size(); //* Returning the total number of items.
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{

        TextView title, description;

        public MyViewHolder(@NotNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.item_title);
            description = itemView.findViewById(R.id.item_description);
        }



    }

}
