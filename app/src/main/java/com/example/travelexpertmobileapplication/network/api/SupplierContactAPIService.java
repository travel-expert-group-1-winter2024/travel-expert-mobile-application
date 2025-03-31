package com.example.travelexpertmobileapplication.network.api;

import com.google.gson.JsonArray;
import com.example.travelexpertmobileapplication.model.SupplierContact;


import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface SupplierContactAPIService {

    @GET("/api/suppliercontacts")
    Call<JsonArray> getSupplierContacts();

    @PUT("/api/suppliercontacts/{id}")
    Call<SupplierContact> updateSupplierContact(@Path("id") int id, @Body SupplierContact updatedContact);

}
