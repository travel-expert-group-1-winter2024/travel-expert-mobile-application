package com.example.travelexpertmobileapplication.model;

import com.google.gson.annotations.SerializedName;

public class Product {

    @SerializedName("id")
    private int productId;

    @SerializedName("prodname")
    private String prodname;

    // Getter
    public int getProductId() {
        return productId;
    }

    public String getProdname() {
        return prodname;
    }

    // Setter
    public void setProductId(int productId) {
        this.productId = productId;
    }

    public void setProdname(String prodname) {
        this.prodname = prodname;
    }
}
