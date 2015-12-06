package com.amrendra.popularmovies.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.amrendra.popularmovies.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Amrendra Kumar on 29/11/15.
 */
public class CustomSpinnerAdapter extends BaseAdapter {
    private List<String> mItems = new ArrayList<>();

    private Context mContext = null;

    LayoutInflater inflater;

    public CustomSpinnerAdapter(Context context) {
        mContext = context;
        inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public void clear() {
        mItems.clear();
    }

    public void addItem(String yourObject) {
        mItems.add(yourObject);
    }

    public void addItems(List<String> yourObjectList) {
        mItems.addAll(yourObjectList);
    }

    @Override
    public int getCount() {
        return mItems.size();
    }

    @Override
    public Object getItem(int position) {
        return mItems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getDropDownView(int position, View view, ViewGroup parent) {
        return createCustomView(position, view, parent, R.layout.toolbar_spinner_item_dropdown, R
                .id.spinner_drop_tv);
    }

    private View createCustomView(int position, View view, ViewGroup parent, int resid, int tvid) {
        if (view == null || !view.getTag().toString().equals("NON_DROPDOWN")) {
            view = inflater.inflate(resid, parent, false);
            view.setTag("NON_DROPDOWN");
        }
        TextView textView = (TextView) view.findViewById(tvid);
        textView.setText(getTitle(position));
        return view;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        return createCustomView(position, view, parent, R.layout.toolbar_spinner_item_actionbar,
                R.id.spinner_actionbar_tv);
    }

    private String getTitle(int position) {
        return position >= 0 && position < mItems.size() ? mItems.get(position) : "";
    }
}
