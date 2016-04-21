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
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.sourceallies.android.zonebeacon.R;
import com.sourceallies.android.zonebeacon.data.DataSource;
import com.sourceallies.android.zonebeacon.data.model.Button;
import com.sourceallies.android.zonebeacon.data.model.Command;
import com.sourceallies.android.zonebeacon.data.model.Gateway;
import com.sourceallies.android.zonebeacon.data.model.Zone;

import java.util.List;

public class EditAdapter extends SectionedRecyclerViewAdapter<EditAdapter.ViewHolder> {

    private Activity activity;

    private Gateway gateway;
    private List<Zone> zones;
    private List<Button> buttons;
    private List<Command> commands;

    private String gatewayTitle;
    private String zonesTitle;
    private String buttonsTitle;
    private String commandsTitle;

    /**
     * Adapter used to display the items in a zone that we are editing
     *
     * @param activity the current activities context
     * @param gateway  gateway that we want to edit
     */
    public EditAdapter(Activity activity, Gateway gateway) {
        this.activity = activity;
        this.gateway = gateway;

        DataSource source = DataSource.getInstance(activity);
        source.open();

        zones = source.findZones(gateway);
        buttons = source.findButtons(gateway);
        commands = source.findCommands(gateway);

        source.close();

        gatewayTitle = activity.getString(R.string.gateway);
        zonesTitle = activity.getString(R.string.zones);
        buttonsTitle = activity.getString(R.string.buttons);
        commandsTitle = activity.getString(R.string.commands);
    }

    /**
     * Get the section count
     *
     * @return 4. Gateway, zones, buttons, commands
     */
    @Override
    public int getSectionCount() {
        return 4;
    }

    /**
     * Get the number of items within a section
     *
     * @param section the section number
     * @return the number of children in the section
     */
    @Override
    public int getItemCount(int section) {
        switch (section) {
            case 0:
                return 1; // gateway
            case 1:
                return zones.size();
            case 2:
                return buttons.size();
            case 3:
                return commands.size();
            default:
                return 0;
        }
    }

    /**
     * Bind the data to the header view
     *
     * @param holder  The recycled view holder
     * @param section the section number
     */
    @Override
    public void onBindHeaderViewHolder(EditAdapter.ViewHolder holder, int section) {
        switch (section) {
            case 0:
                holder.title.setText(gatewayTitle);
                break;
            case 1:
                holder.title.setText(zonesTitle);
                break;
            case 2:
                holder.title.setText(buttonsTitle);
                break;
            case 3:
                holder.title.setText(commandsTitle);
                break;
        }
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
    public void onBindViewHolder(EditAdapter.ViewHolder holder, int section, int relativePosition, int absolutePosition) {
        switch (section) {
            case 0:
                holder.title.setText(gateway.getName());
            case 1:
                holder.title.setText(zones.get(relativePosition).getName());
                break;
            case 2:
                holder.title.setText(buttons.get(relativePosition).getName());
                break;
            case 3:
                holder.title.setText(commands.get(relativePosition).getName());
                break;
        }
    }

    /**
     * Create the view holder object for the item
     *
     * @param parent   the recycler view parent
     * @param viewType VIEW_TYPE_HEADER or VIEW_TYPE_ITEM
     * @return ViewHolder to be used
     */
    @Override
    public EditAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // Change inflated layout based on 'header'
        View v = activity.getLayoutInflater()
                .inflate(viewType == VIEW_TYPE_HEADER ?
                        R.layout.adapter_item_button_zone_header :
                        R.layout.adapter_item_edit, parent, false);
        return new ViewHolder(v);
    }

    /**
     * View holder to go with the recycler view.
     * <p/>
     * This holds the different views so that they do not have to be found every time.
     * It allows them to be recycled.
     */
    protected class ViewHolder extends RecyclerView.ViewHolder {

        @NonNull
        public TextView title;

        @Nullable
        public View root;

        /**
         * Constructor accepting the inflated view.
         *
         * @param itemView inflated view
         */
        public ViewHolder(View itemView) {
            super(itemView);
            root = itemView.findViewById(R.id.root_layout);
            title = (TextView) itemView.findViewById(R.id.title);
        }
    }
}
