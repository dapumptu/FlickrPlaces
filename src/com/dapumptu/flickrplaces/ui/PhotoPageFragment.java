package com.dapumptu.flickrplaces.ui;

import uk.co.senab.photoview.PhotoViewAttacher;
import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.ImageView.ScaleType;

import com.dapumptu.flickrplaces.R;
import com.dapumptu.flickrplaces.image.ImageCache;
import com.dapumptu.flickrplaces.image.ImageFetcher;
import com.dapumptu.flickrplaces.image.ImageWorker.ImageWorkerEventHandler;
import com.dapumptu.flickrplaces.model.DataManager;
import com.dapumptu.flickrplaces.model.PhotoObject;
import com.dapumptu.flickrplaces.util.FlickrUtils;

public class PhotoPageFragment extends Fragment implements ImageWorkerEventHandler {
    private static final String TAG = "ArticlePageFragment";
    private static final String IMAGE_CACHE_DIR = "images";
    
    public static final String ARG_PAGE = "page";
    public static final String ARG_ITEM = "item";
    public static final String ARG_CHANNEL_INDEX = "index";
    
    private int mPageNumber;
    
    // TODO: put local ImageFetcher to PhotoActivity
    private ImageFetcher mImageFetcher;

    private TextView mTitleTextView;
    //private TextView mSummaryTextView;
    private ImageView mPreviewImageView;
    private ProgressBar mProgressBar;
    private PhotoViewAttacher mAttacher;
    
    public static PhotoPageFragment create(int pageNumber) {
        PhotoPageFragment fragment = new PhotoPageFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_PAGE, pageNumber);
        fragment.setArguments(args);
        
        return fragment;
    }
    
    public PhotoPageFragment() {
    }
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        mPageNumber = getArguments().getInt(ARG_PAGE);
        
        // Fetch screen height and width, to use as our max size when loading images as this
        // activity runs full screen
        final DisplayMetrics displayMetrics = new DisplayMetrics();
        this.getActivity().getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        final int height = displayMetrics.heightPixels;
        final int width = displayMetrics.widthPixels;

        // For this sample we'll use half of the longest width to resize our images. As the
        // image scaling ensures the image is larger than this, we should be left with a
        // resolution that is appropriate for both portrait and landscape. For best image quality
        // we shouldn't divide by 2, but this will use more memory and require a larger memory
        // cache.
        final int longest = (height > width ? height : width) / 2;
        
        ImageCache.ImageCacheParams cacheParams =
                new ImageCache.ImageCacheParams(this.getActivity(), IMAGE_CACHE_DIR);
        cacheParams.setMemCacheSizePercent(0.25f); // Set memory cache to 25% of app memory
        
        mImageFetcher = new ImageFetcher(this.getActivity(), longest);
        mImageFetcher.addImageCache(this.getFragmentManager(), cacheParams);
        mImageFetcher.setImageFadeIn(false);
        mImageFetcher.setEventHandler(this);

    }
    
    @Override
    public void onDestroy() {
        Log.d("Fragment", "onDestroy " + this.hashCode());
        super.onDestroy();
    }

    @Override
    public void onPause() {
        Log.d("Fragment", "onPause " + this.hashCode());
        mImageFetcher.setExitTasksEarly(true);
        mImageFetcher.flushCache();
        super.onPause();
    }

    @Override
    public void onResume() {
        Log.d("Fragment", "onResume " + this.hashCode());
        super.onResume();
        mImageFetcher.setExitTasksEarly(false);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        Log.d("Fragment", "onCreateView " + this.hashCode());
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_image_page, container, false);

        mTitleTextView = (TextView) rootView.findViewById(R.id.titleTextView);
        //mSummaryTextView = (TextView) rootView.findViewById(R.id.summaryTextView);
        mPreviewImageView = (ImageView) rootView.findViewById(R.id.previewImageView);
        mProgressBar = (ProgressBar) rootView.findViewById(R.id.progressBar);
        
        mAttacher = new PhotoViewAttacher(mPreviewImageView);
        mAttacher.setScaleType(ScaleType.FIT_END);
        return rootView;
    }
    
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        
        PhotoObject.Photo photo = DataManager.getInstance().getPhotoList().get(mPageNumber);
        mTitleTextView.setText(photo.title);
        
        String photoUrl = FlickrUtils.GetPhotoUrl(photo);
        mImageFetcher.loadImage(photoUrl, mPreviewImageView);
        if (mImageFetcher.isImageCached(photoUrl)) {
            mProgressBar.setVisibility(View.GONE);
            mTitleTextView.setVisibility(View.VISIBLE);
        }
    }
    
    @Override
    public void onAttach(Activity activity) {
        Log.d("Fragment", "onAttach " + this.hashCode());
        super.onAttach(activity);
    }

    @Override
    public void onDestroyView() {
        Log.d("Fragment", "onDestroyView " + this.hashCode());
        mAttacher.cleanup();
        mImageFetcher.closeCache();
        super.onDestroyView();
    }

    @Override
    public void onDetach() {
        Log.d("Fragment", "onDetach " + this.hashCode());
        super.onDetach();
    }

    public int getPageNumber() {
        return mPageNumber;
    }


    @Override
    public void onWorkerFinished() {
        mProgressBar.setVisibility(View.GONE);
        mTitleTextView.setVisibility(View.VISIBLE);
    }
}
