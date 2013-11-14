package com.dapumptu.flickrplaces;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.dapumptu.flickrplaces.model.TopPlaces;
import com.dapumptu.flickrplaces.model.TopPlaces.Place;


public class PlaceListAdapter extends BaseAdapter {

    private LayoutInflater mInflater;
    private List<TopPlaces.Place> mDataList = null;
    
    private static class ViewHolder {
        TextView titleTV;
        TextView summaryTV;
        ImageView thumbnailIV;
    }
    
    public PlaceListAdapter(Context context, List<TopPlaces.Place> dataList) {
        super();
        // Cache the LayoutInflate to avoid asking for a new one each time.
        mInflater = LayoutInflater.from(context);
        mDataList = dataList;
    }
    
    @Override
    public int getCount() {
        return mDataList.size();
    }

    @Override
    public Object getItem(int position) {
        return mDataList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        
        ViewHolder holder;

        if (convertView == null) {
            convertView = mInflater.inflate(android.R.layout.simple_list_item_2, parent, false);

            holder = new ViewHolder();
            holder.titleTV = (TextView) (convertView.findViewById(android.R.id.text1));
            holder.summaryTV = (TextView) (convertView.findViewById(android.R.id.text2));
            //holder.thumbnailIV = (ImageView) (convertView.findViewById(android.R.id.text1));
            
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        if (mDataList != null && mDataList.size() > 0) {
            Place place = mDataList.get(position);
            holder.titleTV.setText(place.getWoeName());
            holder.summaryTV.setText(place.getContent());
        }
        
        return convertView;
    }

}
