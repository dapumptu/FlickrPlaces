
package com.dapumptu.flickrplaces;

import java.util.List;

import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

import com.android.volley.Request.Method;
import com.android.volley.RequestQueue;
import com.android.volley.Response.Listener;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.dapumptu.flickrplaces.model.DataManager;
import com.dapumptu.flickrplaces.model.PhotoObject;
import com.dapumptu.flickrplaces.model.Place;
import com.dapumptu.flickrplaces.util.Downloader;
import com.dapumptu.flickrplaces.util.FlickrJsonParser;
import com.dapumptu.flickrplaces.util.FlickrUtils;

public class PlaceListActivity extends Activity implements Downloader.DownloadTaskListener, Listener<String> {

    //private Downloader mDownloader;
    private RequestQueue mQueue;
    private boolean mInitialized = false;

    private class ListOnItemClickListener implements OnItemClickListener {
        
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            // TODO: move the activity transition code to a mediate class
            final Context context = view.getContext();
            final Intent intent = new Intent(context, PhotoListActivity.class);

            List<Place> placeList = DataManager.getInstance().getPlaceList();
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
    public void onDownloadingFailed() {
        Toast.makeText(this, "RESULT_CONNECTION_ERROR", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onTaskCompleted() {
        updateUi();
    }
    
    @Override
    public void onResponse(String jsonStr) {
        if (jsonStr != null) {
            // TODO: better handling of parsing places and parsing photos JSON
            // files
            List<Place> list = FlickrJsonParser.parsePlaces(jsonStr);
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
