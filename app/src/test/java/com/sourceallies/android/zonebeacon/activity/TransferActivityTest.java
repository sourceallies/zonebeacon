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

import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.view.MenuItem;

import com.sourceallies.android.zonebeacon.ZoneBeaconRobolectricSuite;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.robolectric.Robolectric;

import static org.junit.Assert.*;
import static org.mockito.Mockito.when;

public class TransferActivityTest extends ZoneBeaconRobolectricSuite {

    private TransferActivity activity;

    @Mock
    private MenuItem menuItem;

    @Before
    public void setUp() {
        activity = Robolectric.setupActivity(TransferActivity.class);
    }

    @Test
    public void test_notNull() {
        assertNotNull(activity);
    }

    @Test
    public void test_apiClientNull() {
        assertNull(activity.getClient());
    }

    @Test
    public void test_apiClientNotNullOnSecondLaunch() {
        activity = Robolectric.setupActivity(TransferActivity.class);
        assertNotNull(activity.getClient());
    }

    @Test
    public void test_changedSeenNearbySetting() {
        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(activity);
        assertTrue(sharedPrefs.getBoolean("seen_nearby", false));
    }

    @Test
    public void test_onHomeSelected() {
        when(menuItem.getItemId()).thenReturn(android.R.id.home);
        activity.onOptionsItemSelected(menuItem);
        assertTrue(activity.isFinishing());
    }

    @Test
    public void test_otherOptionSelected() {
        when(menuItem.getItemId()).thenReturn(1);
        activity.onOptionsItemSelected(menuItem);
        assertFalse(activity.isFinishing());
    }

    @Test
    public void test_onConnected() {
        activity.onConnected(null);
    }

    @Test
    public void test_onConnectionSuspended() {
        activity.onConnectionSuspended(0);
    }

    @Test
    public void test_onConnectionFailed() {
        activity.onConnectionFailed(null);
    }

}