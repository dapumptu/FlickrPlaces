
package com.dapumptu.flickrplaces;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ListView;

import com.android.volley.Request.Method;
import com.android.volley.RequestQueue;
import com.android.volley.Response.Listener;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.dapumptu.flickrplaces.model.DataManager;
import com.dapumptu.flickrplaces.model.TopPlaces;
import com.dapumptu.flickrplaces.util.ActivitySwitcher;
import com.dapumptu.flickrplaces.util.FlickrJsonParser;
import com.dapumptu.flickrplaces.util.FlickrUtils;

public class PlaceListActivity extends Activity implements Listener<String> {

    private RequestQueue mQueue;
    private boolean mInitialized = false;
    private boolean mMapViewEnabled = false;
    
    private class ListOnItemClickListener implements OnItemClickListener {
        
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            List<TopPlaces.Place> placeList = DataManager.getInstance().getPlaceList();
            String woeid = placeList.get(position).getWoeId();
            ActivitySwitcher.switchToPhotoList(view.getContext(), woeid);
        }
        
    }
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mQueue = Volley.newRequestQueue(getApplicationContext());
    }

    @Override
    protected void onStart() {
        super.onStart();
        
        if (!mInitialized) {
            mInitialized = true;
            
            // FIXME: the download or parse speed is slow?
            String requestUrl = FlickrUtils.GetTopPlaceRequestUrl();
            mQueue.add(new StringRequest(Method.GET, requestUrl, this, null));
            mQueue.start();
        }
    }

    @Override
    protected void onStop() {
        mQueue.stop();
        super.onStop();
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

    @Override
    public void onResponse(String jsonStr) {
        if (jsonStr != null) {
            List<TopPlaces.Place> list = FlickrJsonParser.parsePlaces(jsonStr);
            
            Collections.sort(list, new Comparator<TopPlaces.Place>() {
                public int compare(TopPlaces.Place o1, TopPlaces.Place o2) {
                    TopPlaces.Place p1 = (TopPlaces.Place) o1;
                    TopPlaces.Place p2 = (TopPlaces.Place) o2;
                    return p1.getWoeName().compareToIgnoreCase(
                            p2.getWoeName());
                }
            });
            
            DataManager.getInstance().setPlaceList(list);
            updateUi();
        }
    }
    
    private void updateUi() {
        BaseAdapter listAdapter = new PlaceListAdapter(PlaceListActivity.this, DataManager.getInstance().getPlaceList());
        ListView lv = new ListView(PlaceListActivity.this);
        lv.setAdapter(listAdapter);
        lv.setOnItemClickListener(new ListOnItemClickListener());
        setContentView(lv);
    }
    
}
