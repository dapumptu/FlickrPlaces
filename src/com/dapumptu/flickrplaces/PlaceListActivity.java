
package com.dapumptu.flickrplaces;

import java.sql.Date;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
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
import com.dapumptu.flickrplaces.image.Utils;
import com.dapumptu.flickrplaces.model.DataManager;
import com.dapumptu.flickrplaces.model.TopPlaces;
import com.dapumptu.flickrplaces.util.ActivitySwitcher;
import com.dapumptu.flickrplaces.util.FlickrJsonParser;
import com.dapumptu.flickrplaces.util.FlickrUtils;

public class PlaceListActivity extends Activity implements Listener<String> {

    private static final String TAG = "PlaceListActivity";

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
        updateData();
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
                    return p1.getWoeName().compareToIgnoreCase(p2.getWoeName());
                }
            });

            DataManager.getInstance().setPlaceList(list);
            updateUi();
        }
    }

    private void updateUi() {
        // TODO: handle null place list
        BaseAdapter listAdapter = new PlaceListAdapter(this, DataManager.getInstance()
                .getPlaceList());
        ListView lv = new ListView(this);
        lv.setAdapter(listAdapter);
        lv.setOnItemClickListener(new ListOnItemClickListener());
        setContentView(lv);
    }

    private void updateData() {
        SharedPreferences settings = getSharedPreferences(Utils.PREFS_GLOBAL, 0);
        long lastTimeStamp = settings.getLong(Utils.KEY_PLACES_UPDATE_TIMESTAMP, 0);
        long timeStamp = System.currentTimeMillis() / 1000L;
        boolean shouldUpdateFromNetwork = false;

        if (!DataManager.getInstance().isPlaceDataCached())
            shouldUpdateFromNetwork = true;
        if (timeStamp - lastTimeStamp > Utils.SECONDS_PER_HOUR)
            shouldUpdateFromNetwork = true;

        Log.d(TAG, "Delta time: " + (timeStamp - lastTimeStamp) + ", current timestamp: "
                + timeStamp + ", last timestamp: " + lastTimeStamp);

        if (shouldUpdateFromNetwork) {
            mInitialized = true;

            SharedPreferences.Editor editor = settings.edit();
            editor.putLong(Utils.KEY_PLACES_UPDATE_TIMESTAMP, timeStamp);
            editor.commit();

            // FIXME: the download or parse speed is slow?
            String requestUrl = FlickrUtils.GetTopPlaceRequestUrl();
            mQueue.add(new StringRequest(Method.GET, requestUrl, this, null));
            mQueue.start();
        } else { // Use data cached in memory
            if (!mInitialized) {
                mInitialized = true;
                updateUi();
            }
        }
    }

}
