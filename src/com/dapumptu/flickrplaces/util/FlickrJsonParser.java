package com.dapumptu.flickrplaces.util;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

import com.dapumptu.flickrplaces.model.PhotoObject;
import com.dapumptu.flickrplaces.model.Place;
import com.dapumptu.flickrplaces.model.PlaceData;
import com.dapumptu.flickrplaces.model.PlaceHolder;
import com.dapumptu.flickrplaces.model.TopPlaces;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class FlickrJsonParser {

    private static final String TAG = "FlickrJsonParser";

//  try {
//  JSONObject jObject = new JSONObject(jsonStr);
//  jObject = jObject.getJSONObject("places");
//  JSONArray jArray = jObject.getJSONArray("place");
//  for (int i = 0; i < jArray.length(); i++) {
//      JSONObject oneObject = jArray.getJSONObject(i);
//      String woeid = oneObject.getString("woeid");
//      String woe_name = oneObject.getString("woe_name");
//      String photo_count = oneObject.getString("photo_count");
//      PlaceData place = new PlaceData(woeid, woe_name, Integer.valueOf(photo_count));
//      // Log.d(TAG, "title: " + woeid + ", link: " + woe_name);
//      placeList.add(place);
//  }
//} catch (JSONException e) {
//  e.printStackTrace();
//}
    
    private FlickrJsonParser() {
        
    }
    
    public static List<Place> parsePlaces(String jsonStr) {

        List<Place> placeList = new ArrayList<Place>();

        Gson gson = new GsonBuilder().create();
        TopPlaces p = gson.fromJson(jsonStr, TopPlaces.class);

        Log.d(TAG, p.places.total);

        // TODO: batch add of places
        for (Place place : p.places.placeList) {
            placeList.add(place);
            // Log.d(TAG, place.woe_name + ' ' + place.woeid);
        }

        return placeList;
    }
    
    public static List<PhotoObject.Photo> parsePhotos(String jsonStr) {

        List<PhotoObject.Photo> photoList = new ArrayList<PhotoObject.Photo>();

         Gson gson = new GsonBuilder().create();
         PhotoObject p = gson.fromJson(jsonStr, PhotoObject.class);
        
         // TODO: batch add of places
         for (PhotoObject.Photo photo : p.photos.photoList) {
             photoList.add(photo);
         }

        return photoList;
    }
}
