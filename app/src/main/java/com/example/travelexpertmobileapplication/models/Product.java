package com.example.travelexpertmobileapplication.models;

public class Product {
    private int productId;
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
