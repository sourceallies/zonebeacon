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
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.sourceallies.android.zonebeacon.R;
import com.sourceallies.android.zonebeacon.data.model.CommandType;

import java.util.ArrayList;
import java.util.List;

/**
 * Adapter that holds the information about the gateways in the database
 */
public class CommandTypeSpinnerAdapter extends BaseAdapter {
    private Activity activity;
    private List<CommandType> commandTypes = new ArrayList<>();

    /**
     * Constructor.
     *
     * @param activity The current context
     * @param commandTypes A list of the gateways that are in the database.
     */
    public CommandTypeSpinnerAdapter(Activity activity, List<CommandType> commandTypes) {
        this.activity = activity;
        this.commandTypes = commandTypes;
    }

    /**
     * Get the number of command types in the database
     *
     * @return number of items in the spinnerAdapter
     */
    @Override
    public int getCount() {
        return commandTypes.size();
    }

    /**
     * Get the command at the selected position
     *
     * @param position Index of the gateway you want to find
     * @return Command from the database list
     */
    @Override
    public CommandType getItem(int position) {
        return commandTypes.get(position);
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
     * @param position Position of the item you want to fill
     * @param view     Recycled view that may or may not have been previously created.
     * @param parent   Parent that holds the views
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
     * @param view     Recycled view that may or may not have been previously created.
     * @param parent   Parent that holds the views
     * @return the view of the currently selected spinner item
     */
    @Override
    public View getView(int position, View view, ViewGroup parent) {
        if (view == null || view.getTag() == null || !view.getTag().toString().equals("NON_DROPDOWN")) {
            // if the view can't be recycled, create a new one
            view = activity.getLayoutInflater()
                    .inflate(R.layout.spinner_item_non_dropdown, parent, false);
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
     * @return Command name or "Create New Command"
     */
    public String getTitle(int position) {
        return getItem(position).getName();
    }
}