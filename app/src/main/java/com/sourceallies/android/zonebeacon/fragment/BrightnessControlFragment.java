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
