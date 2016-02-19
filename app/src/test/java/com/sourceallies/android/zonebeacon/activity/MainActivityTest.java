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
import android.widget.ArrayAdapter;

import com.sourceallies.android.zonebeacon.R;
import com.sourceallies.android.zonebeacon.ZoneBeaconRobolectricSuite;
import com.sourceallies.android.zonebeacon.adapter.GatewaySpinnerAdapter;
import com.sourceallies.android.zonebeacon.data.model.Gateway;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.robolectric.Robolectric;

import java.util.ArrayList;
import java.util.List;

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
    private List<Gateway> gateways = new ArrayList<>();
    private GatewaySpinnerAdapter adapter;

    @Before
    public void setUp() {
        activity = Robolectric.setupActivity(MainActivity.class);
        setupSpinner();
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
    public void test_introResultCancelled() {
        SharedPreferences.Editor e = Mockito.mock(SharedPreferences.Editor.class);

        when(sharedPrefs.edit()).thenReturn(e);
        when(e.putBoolean(anyString(), anyBoolean())).thenReturn(e);
        when(e.commit()).thenReturn(true);

        activity.setSharedPrefs(sharedPrefs);
        activity.onActivityResult(1, Activity.RESULT_CANCELED, new Intent());

        verify(sharedPrefs).edit();
        verify(e).commit();
    }

    @Test
    public void test_canceledResult() {
        activity = Mockito.spy(activity);

        activity.setSharedPrefs(sharedPrefs);
        activity.onActivityResult(2, Activity.RESULT_CANCELED, new Intent());

        verify(activity, times(1)).onActivityResult(eq(2), eq(Activity.RESULT_CANCELED), any(Intent.class));
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

    @Test
    public void test_spinnerSelection() {
        activity.getSpinner().setSelection(2);
        assertEquals(0, activity.getCurrentSpinnerSelection());

        activity.getSpinner().setSelection(1);
        assertEquals(1, activity.getCurrentSpinnerSelection());

        activity.getSpinner().setSelection(2);
        assertEquals(1, activity.getCurrentSpinnerSelection());
    }

    @Test
    public void test_noGateways() {
        adapter = new GatewaySpinnerAdapter(activity, new ArrayList<Gateway>());
        activity.setAdapter(adapter);
        activity.getSpinner().setAdapter(adapter);
        activity.getSpinner().setSelection(0);
        activity.recreate();

        when(sharedPrefs.getBoolean(eq("pref_intro"), eq(false))).thenReturn(false);
        activity.getSpinner().getOnItemSelectedListener().onNothingSelected(null);

        activity.setSharedPrefs(sharedPrefs);
        assertTrue(activity.startIntro());

    }

    private void setupSpinner() {
        Gateway one = new Gateway();
        one.setId(1);
        one.setName("test 1");

        Gateway two = new Gateway();
        two.setId(2);
        two.setName("test 2");

        gateways.add(one);
        gateways.add(two);

        adapter = new GatewaySpinnerAdapter(activity, gateways);
        activity.setAdapter(adapter);
        activity.getSpinner().setAdapter(adapter);
        activity.getSpinner().setSelection(0);
    }

}