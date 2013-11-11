package com.dapumptu.flickrplaces.model;

import java.util.List;

import com.google.gson.annotations.SerializedName;

public class PhotoSearch {

    public Photos photos;
    public PhotoSearch() {

    }
    
    public static class Photos {
        @SerializedName("photo")
        public List<Photo> photoList;

        public Photos() {

        }
    }
    
    public static class Photo {

        // "id": "10681665384",
        // "owner": "23407188@N05",
        // "secret": "bcf8ce9d75",
        // "server": "7336",
        // "farm": 8,
        // "title": "Teddy's Bar and Grill",
        // "ispublic": 1,
        // "isfriend": 0,
        // "isfamily": 0

        @SerializedName("id")
        public String photoId;

        @SerializedName("secret")
        public String secretId;

        @SerializedName("server")
        public String serverId;

        @SerializedName("farm")
        public int farmNum;

        public String title;

        public PhotoContent description;

        public String getTitle() {
            return title;
        }

        public String getDescription() {
            return description.content;
        }

        public static class PhotoContent {
            @SerializedName("_content")
            public String content;

            public PhotoContent() {

            }
        }

        public Photo() {

        }

    }

}