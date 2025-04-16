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
import com.example.travelexpertmobileapplication.fragments.PackageDetailsFragment;
import com.example.travelexpertmobileapplication.model.Package;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import timber.log.Timber;

public class PackageAdapter extends RecyclerView.Adapter<PackageAdapter.MyViewHolder> {

    private List<Package> packageList;
    private Context context;

    public PackageAdapter(List<Package> packageList, Context context) {
        this.packageList = packageList != null ? packageList : new ArrayList<>();
        this.context = context;
    }

    @NonNull
    @Override
    public PackageAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_card, parent, false);
        return new PackageAdapter.MyViewHolder(view); //! Returning a new Viewholder Instance
    }

    @Override
    public void onBindViewHolder(@NonNull PackageAdapter.MyViewHolder holder, int position) {
        //* Grabbing Data
        Package data = packageList.get(position);

        holder.title.setText(data.getPkgname() != null ? data.getPkgname() : "No Name");
        holder.description.setText(data.getPkgdesc() != null ? data.getPkgdesc() : "No Description");


        holder.itemView.setOnClickListener(v -> {
            //* Creating a new instance of the detail fragment
            PackageDetailsFragment packageDetailsFragment = new PackageDetailsFragment();

            //* Passing data using bundles
            Bundle bundle = new Bundle();
            bundle.putSerializable("PackageDetails", data);

            //* Fragment Transaction

            packageDetailsFragment.setArguments(bundle);

            FragmentTransaction transaction = ((FragmentActivity) context).getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.fragment_container, packageDetailsFragment);
            transaction.addToBackStack(null);
            transaction.commit();

        });
    }

    @Override
    public int getItemCount() {
        if (packageList == null) {
            Timber.tag("PackageAdapter").e("Package list is null");
            return 0;
        }
        Timber.tag("PackageAdapter").d("Number of packages fetched: %s", packageList.size());

        return packageList.size(); //* Returning the total number of items.
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView title, description;

        public MyViewHolder(@NotNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.item_title);
            description = itemView.findViewById(R.id.item_description);
        }


    }
}//! Class
