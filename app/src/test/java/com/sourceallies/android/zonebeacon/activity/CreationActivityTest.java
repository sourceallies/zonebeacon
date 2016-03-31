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
import com.sourceallies.android.zonebeacon.fragment.AddButtonFragment;
import com.sourceallies.android.zonebeacon.fragment.AddZoneFragment;
import com.sourceallies.android.zonebeacon.fragment.CommandSetupFragment;
import com.sourceallies.android.zonebeacon.fragment.GatewaySetupFragment;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.robolectric.Robolectric;

import static org.junit.Assert.*;

public class CreationActivityTest extends ZoneBeaconRobolectricSuite {

    private CreationActivity activity;

    @Before
    public void setUp() {
        activity = Robolectric.setupActivity(CreationActivity.class);
        activity = Mockito.spy(activity);
    }

    @Test
    public void test_addGateway() {
        Mockito.doReturn(CreationActivity.TYPE_GATEWAY).when(activity).getFragmentType();
        activity.addSlides();
        assertTrue(activity.getSetupFragment() instanceof GatewaySetupFragment);
    }

    @Test
    public void test_addZone() {
        Mockito.doReturn(CreationActivity.TYPE_ZONE).when(activity).getFragmentType();
        activity.addSlides();
        assertTrue(activity.getSetupFragment() instanceof AddZoneFragment);
    }

    @Test
    public void test_addButton() {
        Mockito.doReturn(CreationActivity.TYPE_BUTTON).when(activity).getFragmentType();
        activity.addSlides();
        assertTrue(activity.getSetupFragment() instanceof AddButtonFragment);
    }

    @Test
    public void test_addCommand() {
        Mockito.doReturn(CreationActivity.TYPE_COMMAND).when(activity).getFragmentType();
        activity.addSlides();
        assertTrue(activity.getSetupFragment() instanceof CommandSetupFragment);
    }
}