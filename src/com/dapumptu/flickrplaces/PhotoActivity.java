package com.dapumptu.flickrplaces;

import android.app.Fragment;
import android.app.FragmentManager;
import android.os.Bundle;
import android.support.v13.app.FragmentStatePagerAdapter;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.DisplayMetrics;
import android.widget.Toast;

import com.dapumptu.flickrplaces.image.ImageCache;
import com.dapumptu.flickrplaces.image.ImageFetcher;
import com.dapumptu.flickrplaces.ui.PhotoPageFragment;

public class PhotoActivity extends FragmentActivity {

    public static final String NUM_PAGES_KEY = "TotalPages";
    public static final String CURRENT_PAGE_KEY = "CurrentPage";
    public static final String WOEID_KEY = "Woeid";
    
    private static final String IMAGE_CACHE_DIR = "images";

    private ViewPager mPager;
    private PagerAdapter mPagerAdapter;
    private ImageFetcher mImageFetcher;
    private String mWoeid = "55992185";

    public ImageFetcher getImageFetcher() {
        return mImageFetcher;
    }

    public String getWoeid() {
        return mWoeid;
    }

    public void setWoeid(String mWoeid) {
        this.mWoeid = mWoeid;
    }
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            mWoeid = bundle.getString(WOEID_KEY, "55992185");
            
            mPagerAdapter = new PhotoPagerAdapter(getFragmentManager(), bundle.getInt(NUM_PAGES_KEY));

            mPager = (ViewPager) findViewById(R.id.pager);
            mPager.setAdapter(mPagerAdapter);
            mPager.setCurrentItem(bundle.getInt(CURRENT_PAGE_KEY));
            mPager.setPageMargin((int) getResources().getDimension(R.dimen.photo_pager_margin));
            mPager.setOffscreenPageLimit(2);
            
            mPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
                @Override
                public void onPageSelected(int position) {
                    Toast.makeText(PhotoActivity.this, "Whoa!", Toast.LENGTH_SHORT);
                }

            });
        }
        
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
    }

    @Override
    protected void onResume() {
        super.onResume();
        mImageFetcher.setExitTasksEarly(false);
    }
    
    @Override
    protected void onPause() {
        mImageFetcher.setExitTasksEarly(true);
        mImageFetcher.flushCache();
        super.onPause();
    }
    
    @Override
    protected void onDestroy() {
        mImageFetcher.closeCache();
        super.onDestroy();
    }

    private class PhotoPagerAdapter extends FragmentStatePagerAdapter {
        private final int mPages;

        public PhotoPagerAdapter(FragmentManager fm, int pages) {
            super(fm);
            mPages = pages;
        }

        @Override
        public Fragment getItem(int position) {
            return PhotoPageFragment.create(position);
        }

        @Override
        public int getCount() {
            return mPages;
        }
    }
}
