
package com.dapumptu.flickrplaces;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
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
import com.dapumptu.flickrplaces.util.FlickrJsonParser;
import com.dapumptu.flickrplaces.util.FlickrUtils;

public class PlaceListActivity extends Activity implements Listener<String> {

    //private Downloader mDownloader;
    private RequestQueue mQueue;
    private boolean mInitialized = false;

    private class ListOnItemClickListener implements OnItemClickListener {
        
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            // TODO: move the activity transition code to a mediate class
            final Context context = view.getContext();
            final Intent intent = new Intent(context, PhotoListActivity.class);

            List<TopPlaces.Place> placeList = DataManager.getInstance().getPlaceList();
            String woeid = placeList.get(position).woeId;
            Bundle bundle = new Bundle();
            bundle.putString(PhotoListActivity.PLACE_WOEID, woeid);
            intent.putExtras(bundle);
            context.startActivity(intent);
        }
        
    }
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //mDownloader = new Downloader();
        mQueue = Volley.newRequestQueue(getApplicationContext());
    }

    @Override
    protected void onStart() {
        super.onStart();
        
        if (!mInitialized) {
            mInitialized = true;
            
            // FIXME: the download or parse speed is slow?
//            mDownloader.download(requestUrl, this);
            
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
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    
    @Override
    public void onResponse(String jsonStr) {
        if (jsonStr != null) {
            List<TopPlaces.Place> list = FlickrJsonParser.parsePlaces(jsonStr);
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
