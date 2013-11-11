
package com.dapumptu.flickrplaces;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;

import com.dapumptu.flickrplaces.model.DataManager;
import com.dapumptu.flickrplaces.model.TopPlaces;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnInfoWindowClickListener;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapActivity extends Activity {

    private GoogleMap mMap;
    private Map<String, String> mMarkerMap;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_map);

        mMap = ((MapFragment) getFragmentManager().findFragmentById(R.id.map)).getMap();
        mMarkerMap = new HashMap<String, String>();
        
        mMap.setOnInfoWindowClickListener(new OnInfoWindowClickListener() {

            @Override
            public void onInfoWindowClick(Marker marker) {

                final Context context = MapActivity.this;
                final Intent intent = new Intent(context, PhotoListActivity.class);
                        //PhotoListActivity.class);

                String woeid = mMarkerMap.get(marker.getId());
                Bundle bundle = new Bundle();
                bundle.putString(PhotoListActivity.PLACE_WOEID, woeid);
                intent.putExtras(bundle);
                context.startActivity(intent);
            }
        });
    }

    @Override
    protected void onStart() {
        // TODO Auto-generated method stub
        super.onStart();
        
        //LatLng sydney = new LatLng(-33.867, 151.206);
        //mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
        //Map.animateCamera(CameraUpdateFactory.newLatLngZoom(sydney, 15), 10, null);
        
        List<TopPlaces.Place> placeList = DataManager.getInstance().getPlaceList();
        for (TopPlaces.Place place : placeList) {
            LatLng geoCoord = new LatLng(Float.valueOf(place.latitude), Float.valueOf(place.longitude));
            Marker marker = mMap.addMarker(new MarkerOptions()
            .position(geoCoord)
            .title(place.woeName)    
            .snippet(place.woeId));
            
            mMarkerMap.put(marker.getId(), place.woeId);
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

}
