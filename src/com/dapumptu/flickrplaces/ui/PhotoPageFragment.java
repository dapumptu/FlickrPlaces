package com.dapumptu.flickrplaces.ui;

import java.util.List;

import uk.co.senab.photoview.PhotoView;
import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.dapumptu.flickrplaces.PhotoActivity;
import com.dapumptu.flickrplaces.R;
import com.dapumptu.flickrplaces.image.ImageFetcher;
import com.dapumptu.flickrplaces.model.DataManager;
import com.dapumptu.flickrplaces.model.PhotoSearch;
import com.dapumptu.flickrplaces.util.FlickrUtils;

public class PhotoPageFragment extends Fragment {
    private static final String TAG = "ArticlePageFragment";
    public static final String ARG_PAGE = "page";
    public static final String ARG_ITEM = "item";
    public static final String ARG_CHANNEL_INDEX = "index";
    
    private ImageFetcher mImageFetcher;
    private TextView mTitleTextView;
    //private TextView mSummaryTextView;
    private PhotoView mPhotoView;
    private ProgressBar mProgressBar;
    
    private int mPageNumber;

    public int getPageNumber() {
        return mPageNumber;
    }
    
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
        mImageFetcher = ((PhotoActivity) getActivity()).getImageFetcher();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        Log.d("Fragment", "onCreateView " + this.hashCode());
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_image_page, container, false);

        mTitleTextView = (TextView) rootView.findViewById(R.id.titleTextView);
//        //mSummaryTextView = (TextView) rootView.findViewById(R.id.summaryTextView);
        mPhotoView = (PhotoView) rootView.findViewById(R.id.previewImageView);
        mProgressBar = (ProgressBar) rootView.findViewById(R.id.progressBar);   

        return rootView;
    }
    
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        
        String woeid = ((PhotoActivity) getActivity()).getWoeid();
        List<PhotoSearch.Photo> list = DataManager.getInstance().getPhotoListFromMap(woeid);
        if (list != null) {
            PhotoSearch.Photo photo = list.get(mPageNumber);
            String title = photo.getTitle();
            String description = photo.getDescription();

            if (title.isEmpty()) {
                title = description.isEmpty() ? "Unknown" : description;
            }
            mTitleTextView.setText(title);

            String photoUrl = FlickrUtils.GetPhotoUrl(photo);
            mImageFetcher.loadImage(photoUrl, mPhotoView);
            // mProgressBar.setVisibility(View.GONE);
            mTitleTextView.setVisibility(View.VISIBLE);
        }
    }

}
