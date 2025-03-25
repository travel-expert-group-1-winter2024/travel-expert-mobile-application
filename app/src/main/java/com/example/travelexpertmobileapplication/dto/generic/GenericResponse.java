package com.example.travelexpertmobileapplication.dto.generic;

import com.google.gson.annotations.SerializedName;

/**
 * A base response class for paginated Spring Data REST responses.
 * This class does not contain embedded items directly.
 * Subclasses should define a concrete embedded field.
 */
public class GenericResponse<T> {
    @SerializedName("_links")
    private Links links;
    private Page page;

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
