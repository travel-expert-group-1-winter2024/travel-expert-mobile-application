package com.example.travelexpertmobileapplication.model;

public class ProductSupplier {
    private int productId;
    private String prodName;
    private int supplierId;
    private String supName;

    private Integer productSupplierId;

    // Constructor
    public ProductSupplier(int productId, String prodName, int supplierId, String supName, Integer productSupplierId) {
        this.productId = productId;
        this.prodName = prodName;
        this.supplierId = supplierId;
        this.supName = supName;
        this.productSupplierId = productSupplierId;
    }

    // Getters and Setters
    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public String getProdName() {
        return prodName;
    }

    public void setProdName(String prodName) {
        this.prodName = prodName;
    }

    public int getSupplierId() {
        return supplierId;
    }

    public void setSupplierId(int supplierId) {
        this.supplierId = supplierId;
    }

    public String getSupName() {
        return supName;
    }

    public void setSupName(String supName) {
        this.supName = supName;
    }

    public Integer getProductSupplierId() {
        return productSupplierId;
    }

    public void setProductSupplierId(Integer productSupplierId) {
        this.productSupplierId = productSupplierId;
    }

    // toString method for debugging
    @Override
    public String toString() {
        return prodName + " - " + supName;
    }
}