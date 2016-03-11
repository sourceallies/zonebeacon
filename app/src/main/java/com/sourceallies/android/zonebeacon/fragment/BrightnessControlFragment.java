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
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.sourceallies.android.zonebeacon.R;

import lombok.Getter;

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

    /**
     * Default constructor for the fragment
     */
    public static BrightnessControlFragment newInstance() {
        return new BrightnessControlFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_brightness_control, container, false);

        // setup the UI elements
        dimmerBar = (SeekBar) root.findViewById(R.id.dimmer);
        dimmerBar.setOnSeekBarChangeListener(this); //add listener to SeekBar
        percent = (TextView) root.findViewById(R.id.percent);
        bulbImg = (ImageView) root.findViewById(R.id.bulb_bottom);

        return root;
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

        // change percent text to current SeekBar value
        //noinspection AndroidLintSetTextI18n
        percent.setText(progress + "%");
        // set the Alpha value of the image equal to the percent value
        float ratio = (float) progress / 100;
        bulbImg.setAlpha(ratio);
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }

}
