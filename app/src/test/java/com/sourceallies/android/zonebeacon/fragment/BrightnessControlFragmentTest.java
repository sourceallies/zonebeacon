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