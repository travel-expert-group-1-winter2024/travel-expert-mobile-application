package com.example.travelexpertmobileapplication.dto.agency;

import com.example.travelexpertmobileapplication.dto.generic.GenericResponse;
import com.example.travelexpertmobileapplication.model.Agency;
import com.google.gson.annotations.SerializedName;

public class AgencyListResponse extends GenericResponse<Agency> {

    @SerializedName("_embedded")
    private AgencyListEmbedded embedded;

    public AgencyListEmbedded getEmbedded() {
        return embedded;
    }

    public void setEmbedded(AgencyListEmbedded embedded) {
        this.embedded = embedded;
    }
}
