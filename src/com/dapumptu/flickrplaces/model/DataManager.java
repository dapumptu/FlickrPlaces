package com.dapumptu.flickrplaces.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DataManager {
    private static final Object sGlobalLock = new Object();
    private static DataManager sInstance;
    
    private List<TopPlaces.Place> mPlaceList;
    private Map<String, List<PhotoSearch.Photo>> mPhotoListMap;
    
    public List<TopPlaces.Place> getPlaceList() {
        return mPlaceList;
    }

    public void setPlaceList(List<TopPlaces.Place> mPlaceList) {
        this.mPlaceList = mPlaceList;
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
        // TODO: remove these new statements
        mPlaceList = new ArrayList<TopPlaces.Place>();
        mPhotoListMap = new HashMap<String, List<PhotoSearch.Photo>>();
    }
    
    public List<PhotoSearch.Photo> getPhotoListFromMap(String key) {
        return mPhotoListMap.get(key);
    }

    public void addPhotoListToMap(String key, List<PhotoSearch.Photo> photoList) {
        mPhotoListMap.put(key, photoList);
    }
    
    public boolean isPlaceDataCached() {
        return ((mPlaceList != null) && (mPlaceList.size() > 0));
    }

    public boolean isPhotoListDataCached(String mWoeid) {
        return (mPhotoListMap.get(mWoeid) != null);
    }

}
