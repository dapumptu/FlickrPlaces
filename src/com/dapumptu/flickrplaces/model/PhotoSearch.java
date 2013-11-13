
package com.dapumptu.flickrplaces.model;

import java.util.List;

import com.google.gson.annotations.SerializedName;

public class PhotoSearch {

    private Photos photos;

    public Photos getPhotos() {
        return photos;
    }
    
    public PhotoSearch() {

    }

    public static class Photos {
        
        @SerializedName("photo")
        private List<Photo> photoList;

        public List<Photo> getPhotoList() {
            return photoList;
        }
        
        public Photos() {

        }
    }

    public static class Photo {

        @SerializedName("id")
        private String photoId;

        @SerializedName("secret")
        private String secretId;

        @SerializedName("server")
        private String serverId;

        @SerializedName("farm")
        private int farmNum;

        private String title;
        private PhotoContent description;
        private String latitude;
        private String longitude;

        public String getPhotoId() {
            return photoId;
        }

        public String getTitle() {
            return (title != null) ? title : "";
        }

        public String getDescription() {
            return (description != null) ? description.content : "";
        }

        public String getServerId() {
            return serverId;
        }

        public String getSecretId() {
            return secretId;
        }

        public String getLatitude() {
            return latitude;
        }

        public String getLongitude() {
            return longitude;
        }

        public static class PhotoContent {
            @SerializedName("_content")
            public String content;

            public PhotoContent() {

            }
        }

        public Photo() {

        }

        public int getFarmNum() {
            return farmNum;
        }

    }

}
