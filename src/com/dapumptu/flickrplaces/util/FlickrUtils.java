package com.dapumptu.flickrplaces.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.content.pm.PackageManager.NameNotFoundException;
import android.util.Base64;
import android.util.Log;

import com.dapumptu.flickrplaces.model.PhotoSearch;

public class FlickrUtils {

    private FlickrUtils() {
        
    }
    
    public static String GetTopPlaceRequestUrl() {
        return "http://api.flickr.com/services/rest/?method=flickr.places.getTopPlacesList&api_key=55f8310d676832b0b7b1e7928c1c00f1&place_type_id=7&format=json&nojsoncallback=1";
    }
    
    public static String GetPhotoListByWoeid(String woeid) {
        String url = String.format("http://api.flickr.com/services/rest/?method=flickr.photos.search&api_key=55f8310d676832b0b7b1e7928c1c00f1&woe_id=%s&per_page=50&extras=description,geo&format=json&nojsoncallback=1", woeid);
        return url;
    }
    
    public static String GetPhotoUrl(PhotoSearch.Photo photo) {
        String url = String.format("http://farm%d.staticflickr.com/%s/%s_%s_m.jpg", photo.getFarmNum(), photo.getServerId(), photo.getPhotoId(), photo.getSecretId());
        return url;
    }
    
}
