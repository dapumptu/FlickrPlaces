package com.dapumptu.flickrplaces.util;

import com.dapumptu.flickrplaces.model.PhotoSearch;

public class FlickrUtils {
    
    // TODO: declare request URL string as static 
    
    private static final String API_KEY = "55f8310d676832b0b7b1e7928c1c00f1";
    private static final String API_METHOD_GETTOPPLACES = "flickr.places.getTopPlacesList";
    private static final String API_METHOD_PHOTOSSEARCH = "flickr.photos.search";
    
    private FlickrUtils() {

    }
    
    public static String GetTopPlaceRequestUrl() {
        int placeTypeId = 7; 
        String url = String.format("http://api.flickr.com/services/rest/?method=%s&api_key=%s&place_type_id=%s&format=json&nojsoncallback=1", 
                API_METHOD_GETTOPPLACES, API_KEY, placeTypeId);
        return url;
    }
    
    public static String GetPhotoListByWoeid(String woeid) {
        int perPage = 50;
        String extrasStr = "description,geo";
        String url = String.format("http://api.flickr.com/services/rest/?method=%s&api_key=%s&per_page=%s&extras=%s&woe_id=%s&format=json&nojsoncallback=1",
                API_METHOD_PHOTOSSEARCH, API_KEY, String.valueOf(perPage), extrasStr, woeid);
        return url;
    }
    
    public static String GetPhotoUrl(PhotoSearch.Photo photo) {
        String url = String.format("http://farm%d.staticflickr.com/%s/%s_%s.jpg", photo.getFarmNum(), photo.getServerId(), photo.getPhotoId(), photo.getSecretId());
        return url;
    }
    
    public static String GetPhotoThumbnailUrl(PhotoSearch.Photo photo) {
        // 150 x 150 thumbnail
        String url = String.format("http://farm%d.staticflickr.com/%s/%s_%s_q.jpg", photo.getFarmNum(), photo.getServerId(), photo.getPhotoId(), photo.getSecretId());
        return url;
    }
    
}
