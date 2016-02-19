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

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.sourceallies.android.zonebeacon.R;
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
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyBoolean;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Matchers.isNull;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
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
        assertEquals("", activity.getTitle());
    }

    @Test
    public void test_viewsCreated() {
        assertNotNull(activity.getRootLayout());
        assertNotNull(activity.getToolbar());
        assertNotNull(activity.getFabMenu());
        assertNotNull(activity.getAddZone());
        assertNotNull(activity.getAddButton());
        assertNotNull(activity.getAddCommand());
        assertNotNull(activity.getSpinner());
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

    @Test
    public void test_adapterSet() {
        assertEquals(activity.getAdapter(), activity.getSpinner().getAdapter());
    }

    @Test
    public void test_expandFab() {
        activity.getFabMenu().expand();
        assertTrue(activity.getFabMenu().isExpanded());

        activity.getAddZone().performClick();
        assertFalse(activity.getFabMenu().isExpanded());
    }

    @Test
    public void test_introResult() {
        SharedPreferences.Editor e = Mockito.mock(SharedPreferences.Editor.class);

        when(sharedPrefs.edit()).thenReturn(e);
        when(e.putBoolean(anyString(), anyBoolean())).thenReturn(e);
        when(e.commit()).thenReturn(true);

        activity.setSharedPrefs(sharedPrefs);
        activity.onActivityResult(1, Activity.RESULT_OK, new Intent());

        verify(sharedPrefs).edit();
        verify(e).commit();
    }

    @Test
    public void test_canceledResult() {
        activity = Mockito.spy(activity);

        activity.setSharedPrefs(sharedPrefs);
        activity.onActivityResult(1, Activity.RESULT_CANCELED, new Intent());

        verify(activity, times(1)).onActivityResult(eq(1), eq(Activity.RESULT_CANCELED), any(Intent.class));
    }

    @Test
    public void test_option_settings() {
        activity = Mockito.spy(activity);
        MenuItem item = Mockito.mock(MenuItem.class);

        when(item.getItemId()).thenReturn(R.id.settings);
        activity.onOptionsItemSelected(item);
        verify(activity).openOption(any(Class.class));
    }

    @Test
    public void test_option_getHelp() {
        activity = Mockito.spy(activity);
        MenuItem item = Mockito.mock(MenuItem.class);

        when(item.getItemId()).thenReturn(R.id.get_help);
        activity.onOptionsItemSelected(item);
        verify(activity).openOption(null);
    }

    @Test
    public void test_option_diagnosis() {
        activity = Mockito.spy(activity);
        MenuItem item = Mockito.mock(MenuItem.class);

        when(item.getItemId()).thenReturn(R.id.diagnosis);
        activity.onOptionsItemSelected(item);
        verify(activity).openOption(null);
    }

    @Test
    public void test_option_transferSettings() {
        activity = Mockito.spy(activity);
        MenuItem item = Mockito.mock(MenuItem.class);

        when(item.getItemId()).thenReturn(R.id.transfer_settings);
        activity.onOptionsItemSelected(item);
        verify(activity).openOption(null);
    }

    @Test
    public void test_option_none() {
        activity = Mockito.spy(activity);
        MenuItem item = Mockito.mock(MenuItem.class);

        when(item.getItemId()).thenReturn(1);
        activity.onOptionsItemSelected(item);
        verify(activity).onOptionsItemSelected(item);
    }

}