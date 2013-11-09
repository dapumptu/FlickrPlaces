package com.dapumptu.flickrplaces;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.dapumptu.flickrplaces.model.PhotoObject;

// Based on
// class List14 in Apidemos
//
public class PhotoListAdapter extends BaseAdapter {

    private Context mContext = null;
    private LayoutInflater mInflater;
    private List<PhotoObject.Photo> mDataList = null;
    
    public List<PhotoObject.Photo> getmDataList() {
        return mDataList;
    }

    public void setDataList(List<PhotoObject.Photo> mDataList) {
        if (mDataList != null) {
            this.mDataList = mDataList;
        }
    }

    private static class ViewHolder {
        TextView titleTV;
        TextView summaryTV;
        ImageView thumbnailIV;
    }
    
    public PhotoListAdapter(Context context, List<PhotoObject.Photo> dataList) {
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

        PhotoObject.Photo photo = mDataList.get(position);
        holder.titleTV.setText(photo.photoId);
        holder.summaryTV.setText(photo.title);
        
        return convertView;
    }

}
