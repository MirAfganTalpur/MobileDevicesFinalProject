package com.mirafgantalpur.onset;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class LocationAdapter extends ArrayAdapter<Location> {
    private Context context;
    private ArrayList<Location> data;

    public LocationAdapter(Context context, ArrayList<Location> data) {
        super(context,0,data);
        this.context = context;
        this.data = data;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return createItemView(position, convertView, parent);
    }

    private View createItemView(int position, View convertView, ViewGroup parent) {
        View listItem = convertView;

        if (listItem == null){
            listItem = LayoutInflater.from(context).inflate(R.layout.listview_item, parent, false);
        }

        Location location = data.get(position);
        String cityCountry = location.getCity() + ", " + location.getCountry();

        TextView nameTV = listItem.findViewById(R.id.viewItem_name);
        nameTV.setText(location.getName());

        TextView cityCountryTV = listItem.findViewById(R.id.viewItem_cityCountry);
        cityCountryTV.setText(cityCountry);

        TextView privPubTV = listItem.findViewById(R.id.viewItem_privatePub);
        if(location.isPrivate()) {
            privPubTV.setText("Private");
        } else {
            privPubTV.setText("Public");
        }

        TextView descTV = listItem.findViewById(R.id.viewItem_desc);
        descTV.setText(location.getFeatures());




        return listItem;
    }
}
