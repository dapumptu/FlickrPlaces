
package com.dapumptu.flickrplaces;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.dapumptu.flickrplaces.model.DataManager;
import com.dapumptu.flickrplaces.model.PhotoSearch;
import com.dapumptu.flickrplaces.model.TopPlaces;
import com.dapumptu.flickrplaces.ui.PhotoPageFragment;
import com.dapumptu.flickrplaces.util.ActivitySwitcher;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnInfoWindowClickListener;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class PhotoMapActivity extends Activity {

    private GoogleMap mMap;
    
    // store the pair: the Marker Id and the index within the photo list
    private Map<String, String> mMarkerMap;

    private boolean mMapViewEnabled = true;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_map);

        mMap = ((MapFragment) getFragmentManager().findFragmentById(R.id.map)).getMap();
        mMarkerMap = new HashMap<String, String>();
        
        mMap.setOnInfoWindowClickListener(new OnInfoWindowClickListener() {

            @Override
            public void onInfoWindowClick(Marker marker) {
                String indexStr = mMarkerMap.get(marker.getId()); 
                int photoCount = DataManager.getInstance().getPhotoList().size();
                int photoIndex = Integer.valueOf(indexStr);
                ActivitySwitcher.switchToPhoto(PhotoMapActivity.this, photoCount, photoIndex);
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

        List<PhotoSearch.Photo> photoList = DataManager.getInstance().getPhotoList();
        int index = 0;
        for (PhotoSearch.Photo photo : photoList) {
            LatLng geoCoord = new LatLng(Float.valueOf(photo.getLatitude()), Float.valueOf(photo.getLongitude()));
            Marker marker = mMap.addMarker(new MarkerOptions()
            .position(geoCoord)
            .title(photo.getTitle())    
            .snippet(photo.getDescription()));
            
            mMarkerMap.put(marker.getId(), String.valueOf(index++));
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.item_toggle_map_view:
                mMapViewEnabled = mMapViewEnabled ? false : true;
                item.setChecked(mMapViewEnabled);

                // TODO: save WOEID in a class field
                String woeid = "55992185";
                Bundle bundle = getIntent().getExtras();
                if (bundle != null) {
                    woeid = bundle.getString(PhotoListActivity.PLACE_WOEID);
                }
                if (mMapViewEnabled)
                    ActivitySwitcher.switchToPhotoMap(this, woeid);
                else
                    ActivitySwitcher.switchToPhotoList(this, woeid);
                
                finish();
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

}
