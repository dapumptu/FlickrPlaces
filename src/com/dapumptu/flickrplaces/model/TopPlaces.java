package com.dapumptu.flickrplaces.model;

import java.util.List;

import com.google.gson.annotations.SerializedName;

public class TopPlaces {
    
    private Places places;
    
    public TopPlaces() {

    }
    
    public Places getPlaces() {
        return places;
    }

    public void setPlaces(Places places) {
        this.places = places;
    }

    public static class Places {
        
        private String total;
        
        @SerializedName("place")
        private List<TopPlaces.Place> placeList;

        public Places() {

        }

        public String getTotal() {
            return total;
        }

        public void setTotal(String total) {
            this.total = total;
        }

        public List<TopPlaces.Place> getPlaceList() {
            return placeList;
        }

        public void setPlaceList(List<TopPlaces.Place> placeList) {
            this.placeList = placeList;
        }
    }
    
    public class Place {
        
        @SerializedName("place_id")
        private String placeId;
        
        @SerializedName("woeid")
        private String woeId; 
        
        @SerializedName("woe_name")
        private String woeName;
        
        @SerializedName("photo_count")
        private String photoCount;
       
        @SerializedName("_content")
        private String content;
        
        private String latitude;
        private String longitude;

        public String getWoeId() {
            return woeId;
        }

        public String getWoeName() {
            return woeName;
        }

        public String getPhotoCount() {
            return photoCount;
        }
        
        public String getContent() {
            return content;
        }

        public Place() {

        }

        public String getLatitude() {
            return latitude;
        }

        public void setLatitude(String latitude) {
            this.latitude = latitude;
        }

        public String getLongitude() {
            return longitude;
        }

        public void setLongitude(String longitude) {
            this.longitude = longitude;
        }
    }

}
