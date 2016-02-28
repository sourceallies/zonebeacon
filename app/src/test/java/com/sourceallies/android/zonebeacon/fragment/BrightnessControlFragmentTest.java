package com.sourceallies.android.zonebeacon.fragment;

import android.support.v4.app.FragmentActivity;
import android.widget.SeekBar;

import com.sourceallies.android.zonebeacon.ZoneBeaconRobolectricSuite;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;

import static org.junit.Assert.*;

public class BrightnessControlFragmentTest extends ZoneBeaconRobolectricSuite {

    @Mock
    FragmentActivity activity;

    BrightnessControlFragment fragment;

    @Before
    public void setUp() {
        fragment = Mockito.spy(new BrightnessControlFragment());
        Mockito.when(fragment.getActivity()).thenReturn(activity);

        startFragment(fragment);
    }

    @Test
    public void test_percent_change_with_slider() {
        setProgress(fragment.getDimmerBar(),50);
        assertTrue(fragment.getPercent().getText() == "50%");
    }

    @Test
    public void test_alpha_change_with_slider() {
        setProgress(fragment.getDimmerBar(),50);
        assertTrue(fragment.getBulbImg().getAlpha() == 0.5f);
    }

    private void setProgress(SeekBar slider, int progress) {
        slider.setProgress(progress);
    }
}