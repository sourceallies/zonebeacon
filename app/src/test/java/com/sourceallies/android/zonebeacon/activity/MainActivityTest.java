/*
 * Copyright (C) 2016 Source Allies
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

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.widget.Toolbar;

import com.sourceallies.android.zonebeacon.ZoneBeaconRobolectricSuite;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.robolectric.Robolectric;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.when;

public class MainActivityTest extends ZoneBeaconRobolectricSuite {

    @Mock
    private SharedPreferences sharedPrefs;
    private MainActivity activity;

    @Before
    public void setUp() {
        activity = Robolectric.setupActivity(MainActivity.class);
    }

    @Test
    public void test_notNull() {
        assertNotNull(activity);
    }

    @Test
    public void test_viewsCreated() {
        assertNotNull(activity.getRootLayout());
        assertNotNull(activity.getToolbar());
        assertNotNull(activity.getFabMenu());
        assertNotNull(activity.getAddZone());
        assertNotNull(activity.getAddButton());
        assertNotNull(activity.getAddCommand());
    }

    @Test
    public void test_startIntro() {
        when(sharedPrefs.getBoolean(eq("pref_intro"), eq(false))).thenReturn(false);

        activity.setSharedPrefs(sharedPrefs);
        assertTrue(activity.startIntro());
    }

    @Test
    public void test_noIntro() {
        when(sharedPrefs.getBoolean(eq("pref_intro"), eq(false))).thenReturn(true);

        activity.setSharedPrefs(sharedPrefs);
        assertFalse(activity.startIntro());
    }

}