package com.dapumptu.flickrplaces;

import java.util.List;

import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.dapumptu.flickrplaces.model.PhotoSearch;


public class PhotoListAdapter extends BaseAdapter {

    private LayoutInflater mInflater;
    private List<PhotoSearch.Photo> mDataList = null;
    
    public List<PhotoSearch.Photo> getmDataList() {
        return mDataList;
    }

    public void setDataList(List<PhotoSearch.Photo> mDataList) {
        if (mDataList != null) {
            this.mDataList = mDataList;
        }
    }

    private static class ViewHolder {
        TextView titleTV;
        TextView summaryTV;
        ImageView thumbnailIV;
    }
    
    public PhotoListAdapter(Context context, List<PhotoSearch.Photo> dataList) {
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
            
            holder.titleTV.setLines(1);
            holder.summaryTV.setLines(2);
            
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        PhotoSearch.Photo photo = mDataList.get(position);
        String title = photo.getTitle();
        String description = photo.getDescription();
        
        if (title.isEmpty()) {
            title = description.isEmpty() ? "Unknown" : description;
        }
        
        holder.titleTV.setText(title);
        holder.summaryTV.setText(Html.fromHtml(description));
        
        return convertView;
    }

}
