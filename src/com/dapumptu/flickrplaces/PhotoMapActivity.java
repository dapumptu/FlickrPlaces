
package com.dapumptu.flickrplaces;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.R.raw;
import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.dapumptu.flickrplaces.image.ImageCache;
import com.dapumptu.flickrplaces.image.ImageFetcher;
import com.dapumptu.flickrplaces.model.DataManager;
import com.dapumptu.flickrplaces.model.PhotoSearch;
import com.dapumptu.flickrplaces.util.ActivitySwitcher;
import com.dapumptu.flickrplaces.util.FlickrUtils;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.InfoWindowAdapter;
import com.google.android.gms.maps.GoogleMap.OnInfoWindowClickListener;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class PhotoMapActivity extends Activity {

    private GoogleMap mMap;
    
    // store the pair: the Marker Id and the index within the photo list
    private Map<String, String> mMarkerMap;

    private static final String IMAGE_CACHE_DIR = "images";
    private ImageFetcher mImageFetcher;
    
    private boolean mMapViewEnabled = true;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_map);

        mMap = ((MapFragment) getFragmentManager().findFragmentById(R.id.map)).getMap();
        mMarkerMap = new HashMap<String, String>();
        
        mMap.setInfoWindowAdapter(new CustomInfoWindowAdapter());
        mMap.setOnInfoWindowClickListener(new OnInfoWindowClickListener() {

            @Override
            public void onInfoWindowClick(Marker marker) {
                String indexStr = mMarkerMap.get(marker.getId()); 
                int photoCount = DataManager.getInstance().getPhotoList().size();
                int photoIndex = Integer.valueOf(indexStr);
                ActivitySwitcher.switchToPhoto(PhotoMapActivity.this, photoCount, photoIndex);
            }
        });
    
        // Init ImageFetcher
        //
        
        // Fetch screen height and width, to use as our max size when loading
        // images as this activity runs full screen
        final DisplayMetrics displayMetrics = new DisplayMetrics();
        this.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        final int height = displayMetrics.heightPixels;
        final int width = displayMetrics.widthPixels;

        final int longest = (height > width ? height : width) / 2;

        ImageCache.ImageCacheParams cacheParams = new ImageCache.ImageCacheParams(this,
                IMAGE_CACHE_DIR);
        cacheParams.setMemCacheSizePercent(0.25f); // Set memory cache to 25% of app memory

        mImageFetcher = new ImageFetcher(this, longest);
        mImageFetcher.addImageCache(this.getFragmentManager(), cacheParams);
        mImageFetcher.setImageFadeIn(true);
        
        Bitmap bitmap = Bitmap.createBitmap(150, 150, Config.RGB_565);
        bitmap.eraseColor(Color.LTGRAY);
        mImageFetcher.setLoadingImage(bitmap);
    }

    @Override
    protected void onStart() {
        super.onStart();

        LatLngBounds.Builder builder = new LatLngBounds.Builder();
        List<PhotoSearch.Photo> photoList = DataManager.getInstance().getPhotoList();
        int index = 0;
        for (PhotoSearch.Photo photo : photoList) {
            LatLng geoCoord = new LatLng(Float.valueOf(photo.getLatitude()), Float.valueOf(photo.getLongitude()));
            Marker marker = mMap.addMarker(new MarkerOptions()
            .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE))
            .position(geoCoord)
            .title(photo.getTitle()));
            
            mMarkerMap.put(marker.getId(), String.valueOf(index++));
            builder.include(marker.getPosition());
        }
        
        // Calculate information needed to zoom the map properly
        LatLngBounds bounds = builder.build();
        int padding = 0; // offset from edges of the map in pixels
        mMap.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds, 200, 200, padding));

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

    private class CustomInfoWindowAdapter implements InfoWindowAdapter {
        private final View mWindow;
        private final View mContents;

        CustomInfoWindowAdapter() {
            mWindow = getLayoutInflater().inflate(R.layout.custom_info_window, null);
            mContents = getLayoutInflater().inflate(R.layout.custom_info_contents, null);
        }

        @Override
        public View getInfoWindow(Marker marker) {
            render(marker, mWindow);
            return mWindow;
        }

        @Override
        public View getInfoContents(Marker marker) {
            render(marker, mContents);
            return mContents;
        }

        private void render(Marker marker, View view) {
            // TODO: refresh the Info window when the photo is downloaded
            ImageView iv = (ImageView) view.findViewById(R.id.badge);
            String indexStr = mMarkerMap.get(marker.getId()); 
            int photoIndex = Integer.valueOf(indexStr);
            PhotoSearch.Photo photo = DataManager.getInstance().getPhotoList().get(photoIndex);
            String photoUrl = FlickrUtils.GetPhotoThumbnailUrl(photo);
            mImageFetcher.loadImage(photoUrl, iv);

            String title = marker.getTitle();
            TextView titleUi = ((TextView) view.findViewById(R.id.title));
            if (title != null) {
                // Spannable string allows us to edit the formatting of the text.
                SpannableString titleText = new SpannableString(title);
                //titleText.setSpan(new ForegroundColorSpan(Color.RED), 0, titleText.length(), 0);
                titleUi.setText(titleText);
            } else {
                titleUi.setText("");
            }

            String snippet = marker.getSnippet();
            TextView snippetUi = ((TextView) view.findViewById(R.id.snippet));
            if (snippet != null && snippet.length() > 12) {
                SpannableString snippetText = new SpannableString(snippet);
                snippetText.setSpan(new ForegroundColorSpan(Color.MAGENTA), 0, 10, 0);
                snippetText.setSpan(new ForegroundColorSpan(Color.BLUE), 12, snippet.length(), 0);
                snippetUi.setText(snippetText);
            } else {
                snippetUi.setText("");
            }
        }
    }
}
