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

package com.sourceallies.android.zonebeacon.fragment;


import android.os.Bundle;
import android.support.annotation.VisibleForTesting;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.sourceallies.android.zonebeacon.R;
import com.sourceallies.android.zonebeacon.api.executor.Executor;
import com.sourceallies.android.zonebeacon.data.DataSource;
import com.sourceallies.android.zonebeacon.data.model.Button;
import com.sourceallies.android.zonebeacon.data.model.Command;
import com.sourceallies.android.zonebeacon.data.model.CommandType;
import com.sourceallies.android.zonebeacon.data.model.Gateway;
import com.sourceallies.android.zonebeacon.data.model.Zone;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

/**
 * DialogFragment that uses a SeekBar to allow the user to dim lights.
 * A TextView displays the brightness level, while an ImageView
 * provides a graphic of a light bulb dimming.
 */
public class BrightnessControlFragment extends DialogFragment implements SeekBar.OnSeekBarChangeListener {

    @Getter
    private SeekBar dimmerBar;
    @Getter
    private TextView percent;
    @Getter
    private ImageView bulbImg;

    private Gateway gateway;

    @Setter
    @Getter
    private Button button;
    @Setter
    @Getter
    private Zone zone;
    @Setter
    private List<CommandType> commandTypes;

    private Executor executor;
    public static final String ARG_GATEWAY_ID = "arg_gateway_id";   // gateway id
    public static final String ARG_IS_ZONE = "arg_is_zone";         // boolean
    public static final String ARG_ITEM_ID = "arg_item_id";         // zone or button id


    /**
     * Default constructor for the fragment
     */
    public static BrightnessControlFragment newInstance(long gatewayId, boolean isZone, long itemId) {
        Bundle args = new Bundle();
        args.putLong(ARG_GATEWAY_ID, gatewayId);
        args.putBoolean(ARG_IS_ZONE, isZone);
        args.putLong(ARG_ITEM_ID, itemId);

        BrightnessControlFragment fragment = new BrightnessControlFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle bundle){
        super.onCreate(bundle);
        Bundle args = getArguments();
        long gatewayId = args.getLong(ARG_GATEWAY_ID);
        boolean isZone = args.getBoolean(ARG_IS_ZONE);
        long itemId = args.getLong(ARG_ITEM_ID);

        DataSource source = DataSource.getInstance(getActivity());
        source.open();

        gateway = source.findGateway(gatewayId);
        executor = Executor.createForGateway(gateway);
        commandTypes = source.findCommandTypesNotShownInUI(gateway);

        if (isZone) {
            List<Zone> zones = source.findZones(gateway);
            setZone(zones, itemId);
        } else {
            List<Button> buttons = source.findButtons(gateway);
            setButton(buttons, itemId);
        }

        source.close();
    }

    /**
     * Given a button list, we want to find the one that matches the itemId
     *
     * @param buttons list of buttons on the gateway
     * @param itemId button id to look for
     */
    @VisibleForTesting
    protected void setButton(List<Button> buttons, long itemId) {
        for (Button button : buttons) {
            if (button.getId() == itemId) {
                this.button = button;
                break;
            }
        }
    }

    /**
     * Given a zone list, find the one that matches the itemId
     * @param zones list of zones in the gateway
     * @param itemId zone id to look for
     */
    @VisibleForTesting
    protected void setZone(List<Zone> zones, long itemId) {
        for (Zone zone : zones) {
            if (zone.getId() == itemId) {
                this.zone = zone;
                break;
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_brightness_control, container, false);

        // setup the UI elements
        percent = (TextView) root.findViewById(R.id.percent);
        bulbImg = (ImageView) root.findViewById(R.id.bulb_bottom);
        dimmerBar = (SeekBar) root.findViewById(R.id.dimmer);

        dimmerBar.setOnSeekBarChangeListener(this);

        return root;
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

        String text = "%s%";
        percent.setText(text.replace("%s", progress + ""));

        // set the alpha level of the image equal to the percent value
        float ratio = (float) progress / 100;
        bulbImg.setAlpha(ratio);

        if (progress < 1) {
            progress = 1;
        } else if (progress > 99) {
            progress = 99;
        }

        if(button != null) {
            addCommands(button, progress);
        } else {
            for (Button button : zone.getButtons()) {
                addCommands(button, progress);
            }
        }

        executor.execute(gateway);
    }

    @VisibleForTesting
    protected void addCommands(Button button, int progress) {
        for (Command command : button.getCommands()) {
            if (command.getControllerNumber() == null) {
                // use the single MCP command type
                command.setCommandType(commandTypes.get(0));
            } else {
                // use the multi MCP command type
                command.setCommandType(commandTypes.get(1));
            }

            executor.addCommand(command, Executor.LoadStatus.OFF, progress);
        }
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }

}
