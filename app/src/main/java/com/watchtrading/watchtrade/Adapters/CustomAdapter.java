package com.watchtrading.watchtrade.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.watchtrading.watchtrade.R;

import java.util.List;


public class CustomAdapter extends BaseAdapter {
    Context context;
    List<String> countryNames;
    LayoutInflater inflter;


    public CustomAdapter(Context applicationContext, List<String> countryNames) {
        this.context = applicationContext;
        this.countryNames = countryNames;

        inflter = (LayoutInflater.from(applicationContext));
    }

    @Override
    public int getCount() {
        return countryNames.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        view = inflter.inflate(R.layout.spinner_item_new, null);
        TextView names = (TextView) view.findViewById(R.id.tvspinnerfff);
        names.setText(countryNames.get(i));

        return view;
    }

}