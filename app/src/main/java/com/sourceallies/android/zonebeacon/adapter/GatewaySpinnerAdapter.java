package com.sourceallies.android.zonebeacon.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.sourceallies.android.zonebeacon.R;
import com.sourceallies.android.zonebeacon.data.model.Gateway;

import java.util.ArrayList;
import java.util.List;

public class GatewaySpinnerAdapter extends BaseAdapter {
    private Activity activity;
    private List<Gateway> gateways = new ArrayList<>();

    public GatewaySpinnerAdapter(Activity activity, List<Gateway> gateways) {
        this.activity = activity;
        this.gateways = gateways;
    }

    @Override
    public int getCount() {
        return gateways.size() + 1;
    }

    @Override
    public Gateway getItem(int position) {
        return position == gateways.size() ? null : gateways.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getDropDownView(int position, View view, ViewGroup parent) {
        if (view == null || view.getTag() == null || !view.getTag().toString().equals("DROPDOWN")) {
            view = activity.getLayoutInflater()
                    .inflate(R.layout.toolbar_spinner_item_dropdown, parent, false);
            view.setTag("DROPDOWN");
        }

        TextView textView = (TextView) view.findViewById(android.R.id.text1);
        textView.setText(getTitle(position));

        return view;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        if (view == null || view.getTag() == null || !view.getTag().toString().equals("NON_DROPDOWN")) {
            view = activity.getLayoutInflater()
                    .inflate(R.layout.toolbar_spinner_item_actionbar, parent, false);
            view.setTag("NON_DROPDOWN");
        }

        TextView textView = (TextView) view.findViewById(android.R.id.text1);
        textView.setText(getTitle(position));

        return view;
    }

    public String getTitle(int position) {
        if (gateways.size() != position) {
            return getItem(position).getName();
        } else {
            return activity.getString(R.string.create_gateway);
        }
    }
}