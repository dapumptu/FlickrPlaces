package com.dapumptu.flickrplaces.model;

import java.util.ArrayList;
import java.util.List;

public class DataManager {
    private static final Object sGlobalLock = new Object();
    private static DataManager sInstance;
    
    private List<Place> mPlaceList;
    private List<PhotoObject.Photo> mPhotoList;
    
    public List<Place> getPlaceList() {
        return mPlaceList;
    }

    public void setPlaceList(List<Place> mPlaceList) {
        this.mPlaceList = mPlaceList;
    }
    
    public List<PhotoObject.Photo> getPhotoList() {
        return mPhotoList;
    }

    public void setPhotoList(List<PhotoObject.Photo> mPhotoList) {
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
        mPlaceList = new ArrayList<Place>();
    }

}
