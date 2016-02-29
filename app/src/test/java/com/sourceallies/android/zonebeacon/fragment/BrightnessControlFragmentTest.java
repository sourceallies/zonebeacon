package com.sourceallies.android.zonebeacon.fragment;

import android.widget.SeekBar;

import com.sourceallies.android.zonebeacon.ZoneBeaconRobolectricSuite;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import static org.junit.Assert.*;
import static org.mockito.Mockito.verifyNoMoreInteractions;

public class BrightnessControlFragmentTest extends ZoneBeaconRobolectricSuite {

    private BrightnessControlFragment fragment;

    @Mock
    private SeekBar seekBar;

    @Before
    public void setUp() {
        fragment = BrightnessControlFragment.newInstance();
        startDialogFragment(fragment);
    }

    @Test
    public void test_percent_change_with_slider() {
        setProgress(fragment.getDimmerBar(), 50);
        assertTrue(fragment.getPercent().getText().toString().equals("50%"));
    }

    @Test
    public void test_alpha_change_with_slider() {
        setProgress(fragment.getDimmerBar(), 50);
        assertTrue(fragment.getBulbImg().getAlpha() == 0.5f);
    }

    @Test
    public void test_startTrackingTouch() {
        fragment.onStartTrackingTouch(seekBar);
        verifyNoMoreInteractions(seekBar);
    }

    @Test
    public void test_stopTrackingTouch() {
        fragment.onStopTrackingTouch(seekBar);
        verifyNoMoreInteractions(seekBar);
    }

    private void setProgress(SeekBar slider, int progress) {
        slider.setProgress(progress);
    }
}