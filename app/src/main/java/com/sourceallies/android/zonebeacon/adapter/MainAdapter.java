/*
 * Copyright (C) 2016 Source Allies, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.sourceallies.android.zonebeacon.adapter;


import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.VisibleForTesting;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SwitchCompat;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.sourceallies.android.zonebeacon.R;
import com.sourceallies.android.zonebeacon.activity.MainActivity;
import com.sourceallies.android.zonebeacon.api.CommandCallback;
import com.sourceallies.android.zonebeacon.api.executor.Executor;
import com.sourceallies.android.zonebeacon.data.StatefulButton;
import com.sourceallies.android.zonebeacon.data.StatefulZone;
import com.sourceallies.android.zonebeacon.data.model.Button;
import com.sourceallies.android.zonebeacon.data.model.Command;
import com.sourceallies.android.zonebeacon.data.model.Gateway;
import com.sourceallies.android.zonebeacon.data.model.Zone;
import com.sourceallies.android.zonebeacon.fragment.BrightnessControlFragment;
import com.sourceallies.android.zonebeacon.util.OnOffStatusUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Adapter used on the MainActivity of the app to display the buttons and zones available.
 * <p/>
 * It uses section headers of "Zone" and "Button", with the corresponding items in each section.
 */
public class MainAdapter extends SectionedRecyclerViewAdapter<MainAdapter.ViewHolder> {

    private Activity context;
    protected Executor executor;

    private String zonesTitle;
    private String buttonsTitle;

    private OnOffStatusUtil statusUtil;

    protected Gateway gateway;
    protected List<StatefulZone> zones;
    protected List<StatefulButton> buttons;

    private List<ViewHolder> viewHolders;

    /**
     * Constructor for the spinnerAdapter.
     *
     * @param context context for the activity
     * @param gateway the current gateway for the adapter
     */
    public MainAdapter(@NonNull Activity context, @NonNull Gateway gateway) {
        this.context = context;
        this.viewHolders = new ArrayList<>();
        this.executor = Executor.createForGateway(gateway);

        this.gateway = gateway;

        zonesTitle = context.getString(R.string.zones);
        buttonsTitle = context.getString(R.string.buttons);
    }

    /**
     * Loads the OnOff statuses of the zones and buttons
     *
     * @param zones list of zones attached to the gateway
     * @param buttons list of buttons attached to the gateway
     * @param loadStatusMap 2D map returned from the status query. First key is the controller number (0 for no controller specified)
     *                      Second key is the load number.
     */
    public void loadOnOffStatuses(@NonNull List<Zone> zones, @NonNull List<Button> buttons,
                                  Map<Integer, Map<Integer, Executor.LoadStatus>> loadStatusMap) {
        statusUtil = getOnOffStatusUtil(zones, buttons, loadStatusMap);
        this.zones = statusUtil.getStatefulZones();
        this.buttons = statusUtil.getStatefulButtons();

        notifyDataSetChanged();
    }

    /**
     * Grab the new load states from the load status utils
     */
    @VisibleForTesting
    protected void updateLoadStatus() {
        this.zones = statusUtil.getStatefulZones();
        this.buttons = statusUtil.getStatefulButtons();

        for (ViewHolder holder : viewHolders) {
            updateHolderSwitch(holder);
        }
    }

    @VisibleForTesting
    protected void updateHolderSwitch(ViewHolder holder) {
        if (isZone(holder.section)) {
            if (shouldToggleSwitch(holder.buttonSwitch.isChecked(),
                    zones.get(holder.relativePosition).getLoadStatus())) {
                holder.buttonSwitch.toggle();
            }
        } else {
            if (shouldToggleSwitch(holder.buttonSwitch.isChecked(),
                    buttons.get(holder.relativePosition).getLoadStatus())) {
                holder.buttonSwitch.toggle();
            }
        }
    }

    @VisibleForTesting
    protected boolean shouldToggleSwitch(boolean isChecked, Executor.LoadStatus newStatus) {
        return (isChecked && newStatus == Executor.LoadStatus.OFF) ||
                (!isChecked && newStatus == Executor.LoadStatus.ON);
    }

    /**
     * Get the status utils to load the On Off status of the buttons and zones
     *
     * @param zones list of zones on the current gateway
     * @param buttons list of buttons on the current gateway
     * @param loadStatusMap status map returned from the status query
     * @return utils used to get the on off status of buttons
     */
    @VisibleForTesting
    protected OnOffStatusUtil getOnOffStatusUtil(@NonNull List<Zone> zones,
                                                 @NonNull List<Button> buttons,
                                                 Map<Integer, Map<Integer, Executor.LoadStatus>> loadStatusMap) {
        return new OnOffStatusUtil(buttons, zones, loadStatusMap);
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
     * @param holder  The recycled view holder
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
     * @param holder           The recycled view holder
     * @param section          section number
     * @param relativePosition number of item within the section
     * @param absolutePosition index out of all non-header items
     */
    @Override
    public void onBindViewHolder(ViewHolder holder, int section, int relativePosition,
                                 int absolutePosition) {
        holder.section = section;
        holder.relativePosition = relativePosition;

        if (isZone(section)) {
            holder.title.setText(zones.get(relativePosition).getZone().getName());
            holder.buttonSwitch.setChecked(
                    zones.get(relativePosition).getLoadStatus() == Executor.LoadStatus.ON
            );
        } else {
            holder.title.setText(buttons.get(relativePosition).getButton().getName());
            holder.buttonSwitch.setChecked(
                    buttons.get(relativePosition).getLoadStatus() == Executor.LoadStatus.ON
            );
        }

        setItemClick(holder.root, holder.buttonSwitch, section, relativePosition);

        if (showFabSpacer(section, relativePosition)) {
            holder.fabSpacer.setVisibility(View.VISIBLE);
        } else if (holder.fabSpacer.getVisibility() != View.GONE) {
            holder.fabSpacer.setVisibility(View.GONE);
        }
    }

    @VisibleForTesting
    protected void setItemClick(View root, final SwitchCompat buttonSwitch,
                                int section, int relativePosition) {
        if (root != null && buttonSwitch != null) { // Null for the header
            root.setOnClickListener(getClickListener(buttonSwitch, section, relativePosition));
            root.setOnLongClickListener(getLongClickListener(buttonSwitch, section, relativePosition));
        }
    }

    @VisibleForTesting
     protected View.OnClickListener getClickListener(final SwitchCompat buttonSwitch,
                                                     final int section, final int relativePosition) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isZone(section)) {
                    executor.addCommands(buttons.get(relativePosition).getButton().getCommands(),
                            getStatus(buttonSwitch));
                    statusUtil.setStates(buttons.get(relativePosition).getButton().getCommands(),
                            getStatus(buttonSwitch) == Executor.LoadStatus.ON ?
                                    Executor.LoadStatus.OFF : Executor.LoadStatus.ON);
                } else {
                    for (Button button : zones.get(relativePosition).getZone().getButtons()) {
                        executor.addCommands(button.getCommands(), getStatus(buttonSwitch));
                        statusUtil.setStates(button.getCommands(),
                                getStatus(buttonSwitch) == Executor.LoadStatus.ON ?
                                        Executor.LoadStatus.OFF : Executor.LoadStatus.ON);
                    }
                }

                executor.execute(gateway);

                // toggle the current button state with an animation
                buttonSwitch.toggle();

                statusUtil.invalidate();
                updateLoadStatus();
            }
        };
    }

    @VisibleForTesting
    protected View.OnLongClickListener getLongClickListener(final SwitchCompat buttonSwitch,
                                                    final int section, final int relativePosition) {
        return new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                ((MainActivity) context).showBrightnessDialog();
                return false;
            }
        };
    }

    /**
     * Tells us whether or not the switch says the light is currently on or not.
     *
     * @param buttonSwitch the switch we want to check the status on
     * @return ON if the switch is currently on, OFF otherwise
     */
    @VisibleForTesting
    protected Executor.LoadStatus getStatus(SwitchCompat buttonSwitch) {
        return buttonSwitch.isChecked() ? Executor.LoadStatus.ON : Executor.LoadStatus.OFF;
    }

    /**
     * Create the view holder object for the item
     *
     * @param parent   the recycler view parent
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
        ViewHolder holder = new ViewHolder(v);

        if (viewType == VIEW_TYPE_ITEM) {
            viewHolders.add(holder);
        }

        return holder;
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
     * If we are in the buttons section and it is the last item in the list, then we need to put
     * a spacer in the list for the FAB.
     *
     * @param section section index
     * @param item item index
     * @return true if we should show the FAB spacer layout, false otherwise
     */
    @VisibleForTesting
    protected boolean showFabSpacer(int section, int item) {
        return !isZone(section) && getItemCount(section) - 1 == item;
    }

    /**
     * View holder to go with the recycler view.
     * <p/>
     * This holds the different views so that they do not have to be found every time.
     * It allows them to be recycled.
     */
    protected class ViewHolder extends RecyclerView.ViewHolder {

        public int section;
        public int relativePosition;

        @NonNull
        public TextView title;

        @Nullable
        public View root;

        @Nullable
        public View fabSpacer;

        @Nullable
        public SwitchCompat buttonSwitch;

        /**
         * Constructor accepting the inflated view.
         *
         * @param itemView inflated view
         */
        public ViewHolder(View itemView) {
            super(itemView);
            root = itemView.findViewById(R.id.root_layout);
            title = (TextView) itemView.findViewById(R.id.title);
            fabSpacer = itemView.findViewById(R.id.fab_spacer);
            buttonSwitch = (SwitchCompat) itemView.findViewById(R.id.button_switch);
        }
    }
}