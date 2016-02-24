package com.sourceallies.android.zonebeacon.fragment;


import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.sourceallies.android.zonebeacon.R;

/**
 * Created by Jeff Osborn on 2/23/2016.
 */
public class BrightnessControlFragment extends DialogFragment implements SeekBar.OnSeekBarChangeListener {

    private SeekBar dimmerBar;
    private TextView percent;
    private ImageView bulbImg;

    /**
     * Default constructor for the fragment
     */
    public BrightnessControlFragment() { }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_brightness_control, container, false);

        // setup the UI elements
        dimmerBar = (SeekBar) root.findViewById(R.id.dimmer);
        dimmerBar.setOnSeekBarChangeListener(this);
        percent = (TextView) root.findViewById(R.id.percent);
        bulbImg = (ImageView) root.findViewById(R.id.bulb_bottom);

        return root;
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

        // change progress text label with current seekbar value
        percent.setText(progress + "%");
        // change action text label to changing
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
