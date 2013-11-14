
package com.dapumptu.flickrplaces;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.LayoutAnimationController;
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
import com.dapumptu.flickrplaces.model.PhotoSearch;
import com.dapumptu.flickrplaces.util.ActivitySwitcher;
import com.dapumptu.flickrplaces.util.FlickrJsonParser;
import com.dapumptu.flickrplaces.util.FlickrUtils;

public class PhotoListActivity extends Activity implements Listener<String> {

    public static final String PLACE_WOEID = "PlaceWoeid";
    
    private ListView mListView;
    private BaseAdapter mListAdapter;
    private RequestQueue mQueue;
    private boolean mInitialized = false;
    private boolean mMapViewEnabled = false;

    private class ListOnItemClickListener implements OnItemClickListener {

        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            ActivitySwitcher.switchToPhoto(view.getContext(), DataManager.getInstance()
                    .getPhotoList().size(), position);
        }

    }
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        mListAdapter = new PhotoListAdapter(this, new ArrayList<PhotoSearch.Photo>());
        mListView = new ListView(this);
        mListView.setAdapter(mListAdapter);
        mListView.setOnItemClickListener(new ListOnItemClickListener());
        setContentView(mListView);

        mQueue = Volley.newRequestQueue(getApplicationContext());
    }

    @Override
    protected void onStart() {
        super.onStart();
        
        if (!mInitialized) {
            mInitialized = true;
            
            // TODO: use a good default WOEID
            String woeid = "55992185";
            Bundle bundle = getIntent().getExtras();
            if (bundle != null) {
                woeid = bundle.getString(PLACE_WOEID);
            }
            
            String requestUrl = FlickrUtils.GetPhotoListByWoeid(woeid);
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

                // TODO: save WOEID in a class field
                String woeid = "55992185";
                Bundle bundle = getIntent().getExtras();
                if (bundle != null) {
                    woeid = bundle.getString(PLACE_WOEID);
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
    
    @Override
    public void onResponse(String jsonStr) {
        if (jsonStr != null) {
            List<PhotoSearch.Photo> list = FlickrJsonParser.parsePhotos(jsonStr);
            DataManager.getInstance().setPhotoList(list);
            ((PhotoListAdapter) mListAdapter).setDataList(list);
            updateUi();
        }
    }
    
    private void updateUi() {
        mListAdapter.notifyDataSetChanged();
        
        playListViewAnimation();
    }

    // TODO: use XML files to define animation
    // Ref: http://blog.csdn.net/sljjyy/article/details/12971999
    // Ref: http://stackoverflow.com/questions/4349803/android-listview-refresh-animation
    private void playListViewAnimation() {

        AnimationSet set = new AnimationSet(true);

        Animation animation = new AlphaAnimation(0.0f, 1.0f);
        animation.setDuration(400);
        set.addAnimation(animation);

//        animation = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0.0f,
//                Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF, -1.0f,
//                Animation.RELATIVE_TO_SELF, 0.0f);
//        animation.setDuration(400);
//        set.addAnimation(animation);

        LayoutAnimationController controller = new LayoutAnimationController(set, 0.25f);
        mListView.setLayoutAnimation(controller);

    }
    
}
