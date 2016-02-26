package com.sourceallies.android.zonebeacon.adapter;


import android.app.Activity;
import android.content.Context;
import android.support.annotation.VisibleForTesting;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.afollestad.sectionedrecyclerview.SectionedRecyclerViewAdapter;
import com.sourceallies.android.zonebeacon.R;
import com.sourceallies.android.zonebeacon.data.model.Button;
import com.sourceallies.android.zonebeacon.data.model.Zone;

import java.util.List;

/**
 * Adapter used on the MainActivity of the app to display the buttons and zones available.
 *
 * It uses section headers of "Zone" and "Button", with the corresponding items in each section.
 */
public class MainAdapter extends SectionedRecyclerViewAdapter<MainAdapter.ViewHolder> {

    private Activity context;

    private String zonesTitle;
    private String buttonsTitle;

    private List<Zone> zones;
    private List<Button> buttons;

    /**
     * Constructor for the spinnerAdapter.
     *
     * @param zones List of zones in the current gateway
     * @param buttons List of buttons in the current gateway
     */
    public MainAdapter(Activity context, List<Zone> zones, List<Button> buttons) {
        this.context = context;

        this.zones = zones;
        this.buttons = buttons;

        zonesTitle = context.getString(R.string.zones);
        buttonsTitle = context.getString(R.string.buttons);
    }

    /**
     * Get the total number of sections
     *
     * @return number of sections in the spinnerAdapter
     */
    @Override
    public int getSectionCount() {
        int count = 0;

        if (zones.size() > 0) count++;
        if (buttons.size() > 0) count++;

        return count;
    }

    /**
     * get the number of items in the section
     *
     * @param section which section/group we want to get the items on
     * @return number of items in that section
     */
    @Override
    public int getItemCount(int section) {
        if (isZone(section))
            return zones.size();
        else
            return buttons.size();
    }

    /**
     * Bind the data to the header view
     *
     * @param holder The recycled view holder
     * @param section the section number
     */
    @Override
    public void onBindHeaderViewHolder(ViewHolder holder, int section) {
        if (isZone(section))
            holder.title.setText(zonesTitle);
        else
            holder.title.setText(buttonsTitle);
    }

    /**
     * Bind the data to the view. Only used for the body views, not headers
     *
     * @param holder The recycled view holder
     * @param section section number
     * @param relativePosition number of item within the section
     * @param absolutePosition index out of all non-header items
     */
    @Override
    public void onBindViewHolder(ViewHolder holder, int section, int relativePosition, int absolutePosition) {
        if (isZone(section))
            holder.title.setText(zones.get(relativePosition).getName());
        else
            holder.title.setText(buttons.get(relativePosition).getName());
    }

    /**
     * Create the view holder object for the item
     *
     * @param parent the recycler view parent
     * @param viewType VIEW_TYPE_HEADER or VIEW_TYPE_ITEM
     * @return ViewHolder to be used
     */
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // Change inflated layout based on 'header'
        View v = context.getLayoutInflater()
                    .inflate(viewType == VIEW_TYPE_HEADER ?
                        R.layout.adapter_item_button_zone_header :
                        R.layout.adapter_item_button_zone, parent, false);
        return new ViewHolder(v);
    }

    /**
     * If the zone size is zero, section zero is the buttons, so this function returns true
     * if the section index we are on is supposed to be a zone.
     *
     * @param section section index
     * @return true if the section is for zones, false otherwise
     */
    @VisibleForTesting
    protected boolean isZone(int section) {
        return section == 0 && zones.size() > 0;
    }

    /**
     * View holder to go with the recycler view.
     *
     * This holds the different views so that they do not have to be found every time.
     * It allows them to be recycled.
     */
    public static class ViewHolder extends RecyclerView.ViewHolder {

        public TextView title;

        /**
         * Constructor accepting the inflated view.
         *
         * @param itemView inflated view
         */
        public ViewHolder(View itemView) {
            super(itemView);
            title = (TextView) itemView.findViewById(R.id.title);
        }
    }
}