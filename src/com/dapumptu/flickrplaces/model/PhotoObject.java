package com.dapumptu.flickrplaces.model;

import java.util.List;

import com.google.gson.annotations.SerializedName;

public class PhotoObject {

    public PhotoHolder photos;
    public PhotoObject() {

    }
    
    public static class PhotoHolder {
        @SerializedName("photo")
        public List<Photo> photoList;

        public PhotoHolder() {

        }
    }
    
    public static class Photo {

//      "id": "10681665384", 
//      "owner": "23407188@N05", 
//      "secret": "bcf8ce9d75", 
//      "server": "7336", 
//      "farm": 8, 
//      "title": "Teddy's Bar and Grill", 
//      "ispublic": 1, 
//      "isfriend": 0, 
//      "isfamily": 0

      
      @SerializedName("id")
      public String photoId;
      
      @SerializedName("secret")
      public String secretId; 
      
      @SerializedName("server")
      public String serverId;
      
      @SerializedName("farm")
      public int farmNum;
      
      @SerializedName("title")
      public String title;
      
      public Photo() {

      }
      
  }
    
}