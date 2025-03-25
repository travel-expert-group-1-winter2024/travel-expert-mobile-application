package com.example.travelexpertmobileapplication.dto.agency;

import com.example.travelexpertmobileapplication.model.Agency;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class AgencyListEmbedded {
    @SerializedName("agencies")
    public List<Agency> agencies;
}
