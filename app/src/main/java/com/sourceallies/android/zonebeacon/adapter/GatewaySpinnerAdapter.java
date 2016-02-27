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

/**
 * Adapter that holds the information about the gateways in the database
 */
public class GatewaySpinnerAdapter extends BaseAdapter {
    private Activity activity;
    private List<Gateway> gateways = new ArrayList<>();

    /**
     * Constructor.
     *
     * @param activity The current context
     * @param gateways A list of the gateways that are in the database.
     */
    public GatewaySpinnerAdapter(Activity activity, List<Gateway> gateways) {
        this.activity = activity;
        this.gateways = gateways;
    }

    /**
     * Get the number of gateways in the database
     * + 1 for an item to "Create New Gateway"
     *
     * @return number of items in the spinnerAdapter
     */
    @Override
    public int getCount() {
        return gateways.size() + 1;
    }

    /**
     * Get the gateway at the selected position
     *
     * @param position Index of the gateway you want to find
     * @return Gateway from the database list or null for the "Create New Gateway" item
     */
    @Override
    public Gateway getItem(int position) {
        return position == gateways.size() ? null : gateways.get(position);
    }

    /**
     * Item's id is its position
     *
     * @param position spinnerAdapter position
     * @return the same number
     */
    @Override
    public long getItemId(int position) {
        return position;
    }

    /**
     * Get the view for the drop down item in the spinner.
     *
     * @param position Posiiton of the item you want to fill
     * @param view Recycled view that may or may not have been previously created.
     * @param parent Parent that holds the views
     *
     * @return the view that you want to display on the spinner dropdown items.
     */
    @Override
    public View getDropDownView(int position, View view, ViewGroup parent) {
        if (view == null || view.getTag() == null || !view.getTag().toString().equals("DROPDOWN")) {
            // if the view can't be recycled, we want to create a new one
            view = activity.getLayoutInflater()
                    .inflate(R.layout.toolbar_spinner_item_dropdown, parent, false);
            view.setTag("DROPDOWN");
        }

        // set the title of the gateway
        TextView textView = (TextView) view.findViewById(android.R.id.text1);
        textView.setText(getTitle(position));

        return view;
    }

    /**
     * Get the view for the non-drop down, displayed item in the spinner.
     *
     * @param position Position of the item you want to fill
     * @param view Recycled view that may or may not have been previously created.
     * @param parent Parent that holds the views
     *
     * @return the view of the currently selected spinner item
     */
    @Override
    public View getView(int position, View view, ViewGroup parent) {
        if (view == null || view.getTag() == null || !view.getTag().toString().equals("NON_DROPDOWN")) {
            // if the view can't be recycled, create a new one
            view = activity.getLayoutInflater()
                    .inflate(R.layout.toolbar_spinner_item_actionbar, parent, false);
            view.setTag("NON_DROPDOWN");
        }

        // set the title based on the gateway name.
        TextView textView = (TextView) view.findViewById(android.R.id.text1);
        textView.setText(getTitle(position));

        return view;
    }

    /**
     * Get the text to display on the spinner items
     *
     * @param position spinnerAdapter position for the item
     * @return Gateway name or "Create New Gateway"
     */
    public String getTitle(int position) {
        if (gateways.size() != position) {
            return getItem(position).getName();
        } else {
            return activity.getString(R.string.create_gateway);
        }
    }
}