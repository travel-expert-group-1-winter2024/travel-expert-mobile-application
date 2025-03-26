package com.example.travelexpertmobileapplication.utils;

import com.example.travelexpertmobileapplication.models.Customer;
import com.google.gson.JsonArray;
import com.example.travelexpertmobileapplication.models.Package;
import com.google.gson.JsonObject;
import com.example.travelexpertmobileapplication.models.SupplierContact;


import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface ApiEndpoints {
    @GET("api/customers")
    Call<JsonArray> getCustomers();

    @PUT("api/customers/{id}")
    Call<Customer> updateCustomer(@Path("id") int id, @Body Customer updatedCustomer);

    @GET("/packages")
    Call<JsonArray> getPackages();

    @POST("/packages")
    Call<Package> createPackage(@Body JsonObject newPackage);

    @GET("/packages/product-supplier")
    Call<JsonArray> getAllProductSuppliers();

    @PUT("/packages/{id}")
    Call<Package> updatePackage(@Path("id") int id, @Body JsonObject updatedPackage);

    @DELETE("/packages/{id}")
    Call<Void> deletePackage(@Path("id") int id);

    //  Endpoints for Product and Supplier

    @GET("/products")
    Call<JsonArray> getProducts();

    @GET("/suppliercontacts")
    Call<JsonArray> getSupplierContacts();

    @PUT("/suppliercontacts/{id}")
    Call<SupplierContact> updateSupplierContact(@Path("id") int id, @Body SupplierContact updatedContact);


//    // Example GET request with a dynamic ID
//    @GET("users/{id}")
//    Call<User> getUserById(@Path("id") int id);
//
//    // Example POST request
//    @POST("users")
//    Call<User> createUser(@Body User user);
}
