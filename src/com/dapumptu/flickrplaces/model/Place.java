package com.dapumptu.flickrplaces.model;

import com.google.gson.annotations.SerializedName;

public class Place {
    
    @SerializedName("place_id")
    public String placeId;
    
    @SerializedName("woeid")
    public String woeId; 
    
    @SerializedName("woe_name")
    public String woeName;
    
    @SerializedName("photo_count")
    public String photoCount;
    
    public String latitude;
    public String longitude;
    
//    public String place_type;
//    public String place_type_id;
//    public String timezone;
//    public String _content;

//    "woeid": "22722087", 
//    "latitude": -32.922, 
//    "longitude": "151.731", 
//    "place_url": "\/Australia\/New+South+Wales\/Newcastle\/Broadmeadow", 
//    "place_type": "neighbourhood", 
//    "place_type_id": 22, 
//    "timezone": "Australia\/Sydney", 
//    "_content": "Broadmeadow, Newcastle, NSW, AU, Australia", 
//    "woe_name": "Broadmeadow", 
//    "photo_count": "522"

    public Place() {

    }

    public String getWoeId() {
        // TODO Auto-generated method stub
        return woeId;
    }

    public String getWoeName() {
        // TODO Auto-generated method stub
        return woeName;
    }

    public String getPhotoCount() {
        // TODO Auto-generated method stub
        return photoCount;
    }
}
