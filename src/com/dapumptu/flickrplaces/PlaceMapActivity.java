
package com.dapumptu.flickrplaces;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.dapumptu.flickrplaces.model.DataManager;
import com.dapumptu.flickrplaces.model.TopPlaces;
import com.dapumptu.flickrplaces.util.ActivitySwitcher;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnInfoWindowClickListener;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class PlaceMapActivity extends Activity {

    private GoogleMap mMap;
    
    // store the pair: Marker Id and the WOEID of the place
    private Map<String, String> mMarkerMap;

    private boolean mMapViewEnabled = true;
    
    // TODO: define the MapAdapter to populate data
    // and handle user interaction with the map
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_map);

        mMap = ((MapFragment) getFragmentManager().findFragmentById(R.id.map)).getMap();
        mMarkerMap = new HashMap<String, String>();
        
        mMap.setOnInfoWindowClickListener(new OnInfoWindowClickListener() {

            @Override
            public void onInfoWindowClick(Marker marker) {
                String woeid = mMarkerMap.get(marker.getId());
                ActivitySwitcher.switchToPhotoList(PlaceMapActivity.this, woeid);
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        
        List<TopPlaces.Place> placeList = DataManager.getInstance().getPlaceList();
        for (TopPlaces.Place place : placeList) {
            LatLng geoCoord = new LatLng(Float.valueOf(place.getLatitude()), Float.valueOf(place.getLongitude()));
            Marker marker = mMap.addMarker(new MarkerOptions()
            .position(geoCoord)
            .title(place.getWoeName())    
            .snippet(place.getWoeId()));
            
            mMarkerMap.put(marker.getId(), place.getWoeId());
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
                
                if (mMapViewEnabled)
                    ActivitySwitcher.switchToPlaceMap(this);
                else
                    ActivitySwitcher.switchToPlaceList(this);
                
                finish();
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

}
