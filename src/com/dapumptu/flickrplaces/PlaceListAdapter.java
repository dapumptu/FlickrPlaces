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

// Based on
// class List14 in Apidemos
//
public class PlaceListAdapter extends BaseAdapter {

    private Context mContext = null;
    private LayoutInflater mInflater;
    private List<TopPlaces.Place> mDataList = null;
    
    private static class ViewHolder {
        TextView titleTV;
        TextView summaryTV;
        ImageView thumbnailIV;
    }
    
    public PlaceListAdapter(Context context, List<TopPlaces.Place> dataList) {
        super();
        mContext = context;
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
        
        // A ViewHolder keeps references to children views to avoid unnecessary calls
        // to findViewById() on each row.
        ViewHolder holder;

        // When convertView is not null, we can reuse it directly, there is no need
        // to reinflate it. We only inflate a new View when the convertView supplied
        // by ListView is null.
        if (convertView == null) {
            convertView = mInflater.inflate(android.R.layout.simple_list_item_2, parent, false);

            // Creates a ViewHolder and store references to the two children views
            // we want to bind data to.
            holder = new ViewHolder();
            holder.titleTV = (TextView) (convertView.findViewById(android.R.id.text1));
            holder.summaryTV = (TextView) (convertView.findViewById(android.R.id.text2));
            //holder.thumbnailIV = (ImageView) (convertView.findViewById(android.R.id.text1));
            
            convertView.setTag(holder);
        } else {
            // Get the ViewHolder back to get fast access to the TextView
            // and the ImageView.
            holder = (ViewHolder) convertView.getTag();
        }

        if (mDataList != null && mDataList.size() > 0) {
            Place place = mDataList.get(position);
            holder.titleTV.setText(place.getWoeName());
            holder.summaryTV.setText(place.getCotent());
        }
        
        return convertView;
    }

}
