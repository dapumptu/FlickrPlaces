package com.dapumptu.flickrplaces.model;


/**
 * Store "phoro" data from the JSON response of flickr.places.getTopPlacesList
 * @
 * "place_id": "83UbKq1UV7I51F6p5g", 
 * "woeid": "55806745", 
 * "latitude": 33.805, 
 * "longitude": -117.911, 
 * "place_url": "\/United+States\/California\/Anaheim\/Anaheim+Resort", 
 * "place_type": "neighbourhood", 
 * "place_type_id": 22, 
 * "timezone": "America\/Los_Angeles", 
 * "_content": "Anaheim Resort, Anaheim, CA, US, United States", 
 * "woe_name": "Anaheim Resort", 
 * "photo_count": "347"
 * 
 */
public class PlaceData {

    private String woeId;
    private String woeName;
    private int photoCount;
    
    public String getWoeId() {
        return woeId;
    }

//    public void setWoeId(String woeId) {
//        this.woeId = woeId;
//    }

    public String getWoeName() {
        return woeName;
    }

//    public void setWoeName(String woeName) {
//        this.woeName = woeName;
//    }

    public int getPhotoCount() {
        return photoCount;
    }

//    public void setPhotoCount(String photoCount) {
//        this.photoCount = photoCount;
//    }

    public PlaceData(String woeId, String woeName, int photoCount) {
        this.woeId = woeId;
        this.woeName = woeName;
        this.photoCount = photoCount;
    }

}
