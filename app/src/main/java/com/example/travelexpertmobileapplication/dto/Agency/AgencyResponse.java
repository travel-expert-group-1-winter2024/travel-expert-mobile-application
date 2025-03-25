package com.example.travelexpertmobileapplication.dto.Agency;

import com.example.travelexpertmobileapplication.dto.Generic.Links;
import com.example.travelexpertmobileapplication.dto.Generic.Page;
import com.google.gson.annotations.SerializedName;

public class AgencyResponse {
    @SerializedName("_embedded")
    private Embedded embedded;
    @SerializedName("_links")
    private Links links;
    private Page page;

    public AgencyResponse() {
    }

    public Embedded getEmbedded() {
        return embedded;
    }

    public void setEmbedded(Embedded embedded) {
        this.embedded = embedded;
    }

    public Links getLinks() {
        return links;
    }

    public void setLinks(Links links) {
        this.links = links;
    }

    public Page getPage() {
        return page;
    }

    public void setPage(Page page) {
        this.page = page;
    }
}
