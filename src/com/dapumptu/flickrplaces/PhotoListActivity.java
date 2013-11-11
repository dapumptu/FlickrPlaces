
package com.dapumptu.flickrplaces;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
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
import com.dapumptu.flickrplaces.util.FlickrJsonParser;
import com.dapumptu.flickrplaces.util.FlickrUtils;

public class PhotoListActivity extends Activity implements Listener<String> {

    public static final String PLACE_WOEID = "PlaceWoeid";
    
    private ListView mListView;
    private BaseAdapter mListAdapter;
    private RequestQueue mQueue;
    private boolean mInitialized = false;

    private class ListOnItemClickListener implements OnItemClickListener {
        
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//            final Context context = view.getContext();
//            final Intent intent = new Intent(context, ImageActivity.class);
//
//            Bundle bundle = new Bundle();
//            bundle.putInt(ImageActivity.PHOTO_INDEX, position);
//            intent.putExtras(bundle);
//            context.startActivity(intent);

            final Context context = view.getContext();
            final Intent intent = new Intent(context, PhotoActivity.class);

            Bundle bundle = new Bundle();
            bundle.putInt(PhotoActivity.NUM_PAGES_KEY, DataManager.getInstance().getPhotoList().size());
            bundle.putInt(PhotoActivity.CURRENT_PAGE_KEY, position);

            intent.putExtras(bundle);
            context.startActivity(intent);
        }
        
    }
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        mListAdapter = new PhotoListAdapter(PhotoListActivity.this, new ArrayList<PhotoSearch.Photo>());
        mListView = new ListView(PhotoListActivity.this);
        mListView.setAdapter(mListAdapter);
        mListView.setOnItemClickListener(new ListOnItemClickListener());
        setContentView(mListView);

        //mDownloader = new Downloader();
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
            //mDownloader.download(requestUrl, this);
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
