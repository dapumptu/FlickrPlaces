package com.dapumptu.flickrplaces.util;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.dapumptu.flickrplaces.PhotoActivity;
import com.dapumptu.flickrplaces.PhotoListActivity;
import com.dapumptu.flickrplaces.PhotoMapActivity;
import com.dapumptu.flickrplaces.PlaceListActivity;
import com.dapumptu.flickrplaces.PlaceMapActivity;

public class ActivitySwitcher {

    private ActivitySwitcher() {
        
    }
    
    public static void switchToPlaceList(Context context) {
        final Intent intent = new Intent(context, PlaceListActivity.class);
        context.startActivity(intent);
    }
    
    public static void switchToPhotoList(Context context, String woeid) {
        final Intent intent = new Intent(context, PhotoListActivity.class);

        Bundle bundle = new Bundle();
        bundle.putString(PhotoListActivity.PLACE_WOEID, woeid);
        
        intent.putExtras(bundle);
        context.startActivity(intent);
    }
    
    public static void switchToPlaceMap(Context context) {
        final Intent intent = new Intent(context, PlaceMapActivity.class);
        context.startActivity(intent);
    }
    
    public static void switchToPhotoMap(Context context, String woeid) {
        final Intent intent = new Intent(context, PhotoMapActivity.class);

        Bundle bundle = new Bundle();
        bundle.putString(PhotoListActivity.PLACE_WOEID, woeid);
        
        intent.putExtras(bundle);
        context.startActivity(intent);
    }
    
    public static void switchToPhoto(Context context, int photoCount, int photoIndex) {
        final Intent intent = new Intent(context, PhotoActivity.class);

        Bundle bundle = new Bundle();
        bundle.putInt(PhotoActivity.NUM_PAGES_KEY, photoCount);
        bundle.putInt(PhotoActivity.CURRENT_PAGE_KEY, photoIndex);

        intent.putExtras(bundle);
        context.startActivity(intent);
    }
}
