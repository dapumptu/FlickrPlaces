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

import com.dapumptu.flickrplaces.model.PhotoObject;

public class FlickrUtils {

    private FlickrUtils() {
        
    }
    
    public static String GetTopPlaceRequestUrl() {
        return "http://api.flickr.com/services/rest/?method=flickr.places.getTopPlacesList&api_key=55f8310d676832b0b7b1e7928c1c00f1&place_type_id=22&format=json&nojsoncallback=1";
    }
    
    public static String GetPhotoListByWoeid(String woeid) {
        String url = String.format("http://api.flickr.com/services/rest/?method=flickr.photos.search&api_key=55f8310d676832b0b7b1e7928c1c00f1&woe_id=%s&format=json&nojsoncallback=1", woeid);
        return url;
    }
    
    public static String GetPhotoUrl(PhotoObject.Photo photo) {
        String url = String.format("http://farm%d.staticflickr.com/%s/%s_%s_m.jpg", photo.farmNum, photo.serverId, photo.photoId, photo.secretId);
        return url;
    }
    
  //"http://where.yahooapis.com/v1/place/2487956?&appid=gGOZvLPV34EzEgsZtcXOyCU6.iQXtxc77E3GG652nqkgeOtm64H6I.6sUiOT6E3vFG1NaaopbI5iK9ptii4gHg0RkpdI4Ps-";
    
    // TODO: Put this method to a Utils class
    public static void printKeyHash(Context context) {
        // Add code to print out the key hash
        try {
            PackageInfo info = context.getPackageManager().getPackageInfo(
                    "com.dapumptu.khtdoodle", PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.d("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        } catch (NameNotFoundException e) {

        } catch (NoSuchAlgorithmException e) {

        }
    }
}
