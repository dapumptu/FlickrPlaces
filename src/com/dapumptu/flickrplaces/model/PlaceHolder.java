package com.dapumptu.flickrplaces.model;

import java.util.List;

import com.google.gson.annotations.SerializedName;

public class PlaceHolder {
    public String total;
    
    @SerializedName("place")
    public List<Place> placeList;

    public PlaceHolder() {

    }
}
