package com.dapumptu.flickrplaces.model;

import java.util.ArrayList;
import java.util.List;

public class DataManager {
    private static final Object sGlobalLock = new Object();
    private static DataManager sInstance;
    
    private List<TopPlaces.Place> mPlaceList;
    private List<PhotoSearch.Photo> mPhotoList;
    
    public List<TopPlaces.Place> getPlaceList() {
        return mPlaceList;
    }

    public void setPlaceList(List<TopPlaces.Place> mPlaceList) {
        this.mPlaceList = mPlaceList;
    }
    
    public List<PhotoSearch.Photo> getPhotoList() {
        return mPhotoList;
    }

    public void setPhotoList(List<PhotoSearch.Photo> mPhotoList) {
        this.mPhotoList = mPhotoList;
    }
    
    // Thread-safe with lazy-loading
    //
    public static DataManager getInstance() {
        synchronized (sGlobalLock) {
            if (sInstance == null) {
                sInstance = new DataManager();
            }
            return sInstance;
        }
    }

    private DataManager() {
        mPlaceList = new ArrayList<TopPlaces.Place>();
    }

}
