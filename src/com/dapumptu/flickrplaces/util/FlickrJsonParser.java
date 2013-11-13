package com.dapumptu.flickrplaces.util;

import java.util.ArrayList;
import java.util.List;

import android.util.Log;

import com.dapumptu.flickrplaces.model.PhotoSearch;
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
    
    public static List<TopPlaces.Place> parsePlaces(String jsonStr) {

        List<TopPlaces.Place> placeList = new ArrayList<TopPlaces.Place>();

        Gson gson = new GsonBuilder().create();
        TopPlaces p = gson.fromJson(jsonStr, TopPlaces.class);

        Log.d(TAG, p.getPlaces().getTotal());

        // TODO: batch add of places
        for (TopPlaces.Place place : p.getPlaces().getPlaceList()) {
            placeList.add(place);
            // Log.d(TAG, place.woe_name + ' ' + place.woeid);
        }

        return placeList;
    }
    
    public static List<PhotoSearch.Photo> parsePhotos(String jsonStr) {

        List<PhotoSearch.Photo> photoList = new ArrayList<PhotoSearch.Photo>();

         Gson gson = new GsonBuilder().create();
         PhotoSearch p = gson.fromJson(jsonStr, PhotoSearch.class);
        
         // TODO: batch add of places
         for (PhotoSearch.Photo photo : p.getPhotos().getPhotoList()) {
             photoList.add(photo);
         }

        return photoList;
    }
}
