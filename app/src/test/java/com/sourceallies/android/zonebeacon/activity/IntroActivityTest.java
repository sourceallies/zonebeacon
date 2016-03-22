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

package com.sourceallies.android.zonebeacon.activity;

import com.sourceallies.android.zonebeacon.ZoneBeaconRobolectricSuite;
import com.sourceallies.android.zonebeacon.fragment.GatewaySetupFragment;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.robolectric.Robolectric;

import static org.junit.Assert.*;

public class IntroActivityTest extends ZoneBeaconRobolectricSuite {

    private IntroActivity activity;

    @Before
    public void setUp() {
        activity = Robolectric.setupActivity(IntroActivity.class);
    }

    @Test
    public void test_notNull() {
        assertNotNull(activity);
        activity.onNextPressed();
    }

    @Test
    public void test_twoFragments() {
        assertEquals(2, activity.getViewPager().getAdapter().getCount());
    }

    @Test
    public void test_formFilled() {
        IntroActivity spy = Mockito.spy(activity);

        ((GatewaySetupFragment)spy.getSetupFragment()).getName().getEditText().setText("Test Gateway");
        ((GatewaySetupFragment)spy.getSetupFragment()).getIpAddress().getEditText().setText("192.168.1.100");
        ((GatewaySetupFragment)spy.getSetupFragment()).getPort().getEditText().setText("11000");

        spy.onDonePressed();

        Mockito.verify(spy, Mockito.times(1)).save();
    }

    @Test
    public void test_formError() {
        IntroActivity spy = Mockito.spy(activity);

        ((GatewaySetupFragment)spy.getSetupFragment()).getName().getEditText().setText("");
        ((GatewaySetupFragment)spy.getSetupFragment()).getIpAddress().getEditText().setText("192.168.1.100");
        ((GatewaySetupFragment)spy.getSetupFragment()).getPort().getEditText().setText("11000");

        spy.onDonePressed();

        Mockito.verify(spy, Mockito.times(0)).save();
    }

}