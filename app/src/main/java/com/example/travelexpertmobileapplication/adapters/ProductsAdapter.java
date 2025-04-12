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
import com.example.travelexpertmobileapplication.fragments.ProductDetailsFragment;
import com.example.travelexpertmobileapplication.model.Product;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import timber.log.Timber;

public class ProductsAdapter extends RecyclerView.Adapter<SupplierAdapter.MyViewHolder> {

    private List<Product> productList;
    private Context context;

    //* Constructor
    public ProductsAdapter(List<Product> productList, Context context){
        this.productList = productList != null ? productList : new ArrayList<>();
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
    public SupplierAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //* Inflating the card layout for each item
        View view = LayoutInflater.from(context).inflate(R.layout.item_card, parent, false);
        return new SupplierAdapter.MyViewHolder(view); //! Returning a new Viewholder Instance
    }

    @Override
    public void onBindViewHolder(@NonNull SupplierAdapter.MyViewHolder holder, int position) {
        //* Grabbing the data to start the process
        Product data = productList.get(position);
        holder.title.setText(data.getProdname() != null ? data.getProdname() : "No Name");
        int productId = data.getProductId();
        holder.description.setText(productId != 0 ? String.valueOf(productId) : "No ID");
        Timber.tag("Products Adapter").e("productId:%s", productId);

        //* Event Handler

        holder.itemView.setOnClickListener(v -> {
            //* Creating a new instance of the detail fragment
            ProductDetailsFragment productDetailsFragment = new ProductDetailsFragment();

            //* Passing data with bundles
            Bundle bundle = new Bundle();

            bundle.putInt("id", data.getProductId());
            bundle.putString("prodName", data.getProdname());

            productDetailsFragment.setArguments(bundle);

            //* Fragment Transaction
            FragmentTransaction transaction = ((FragmentActivity)context).getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.fragment_container, productDetailsFragment);
            transaction.addToBackStack(null);
            transaction.commit();
        });
    }

    @Override
    public int getItemCount() {
        if (productList == null){
            Timber.e("ProductAdapter: productList is null");
            return 0;
        }
        Timber.tag("ProductAdapter").d("Number of products fetched: " + productList.size());
        return productList.size();
    }


}//! Class
