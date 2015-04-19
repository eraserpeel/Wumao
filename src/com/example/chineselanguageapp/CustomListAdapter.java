package com.example.chineselanguageapp;

import java.util.ArrayList;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ProgressBar;
import android.widget.TextView;

public class CustomListAdapter extends BaseAdapter {
 
    private ArrayList listData;
    private LayoutInflater layoutInflater;
    private int selectedIndex; 
 
    public CustomListAdapter(Context context, ArrayList listData) {
        this.listData = listData;
        layoutInflater = LayoutInflater.from(context);
    }
    
    public void setSelectedIndex(int ind) {
        selectedIndex = ind;
        notifyDataSetChanged();
    }
    
    @Override
    public int getCount() {
        return listData.size();
    }
 
    @Override
    public Object getItem(int position) {
        return listData.get(position);
    }
 
    @Override
    public long getItemId(int position) {
        return position;
    }
 
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        
        SettingsRowItem theItem = (SettingsRowItem)getItem(position); 
        
        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.custom_listview, null);
            holder = new ViewHolder();
            holder.nameDb = (TextView) convertView.findViewById(R.id.txtDatabase);
            holder.progressBar = (ProgressBar) convertView.findViewById(R.id.progressBar);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        
        holder.nameDb.setText(theItem.itemText);
        holder.progressBar.setProgress(theItem.itemProgress);
        
        return convertView;
    }
 
    static class ViewHolder {
        TextView nameDb;
        ProgressBar progressBar;
    }
 
}