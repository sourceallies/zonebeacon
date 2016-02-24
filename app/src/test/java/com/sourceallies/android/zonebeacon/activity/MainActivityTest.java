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

import com.getbase.floatingactionbutton.FloatingActionsMenu;
import com.sourceallies.android.zonebeacon.R;
import com.sourceallies.android.zonebeacon.ZoneBeaconRobolectricSuite;
import com.sourceallies.android.zonebeacon.adapter.GatewaySpinnerAdapter;
import com.sourceallies.android.zonebeacon.data.DataSource;
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
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.spy;
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
        activity = spy(activity);

        adapter = createAdapter();
        activity.setAdapter(adapter);
        activity.getSpinner().setAdapter(adapter);
    }

    @Test
    public void test_notNull() {
        assertNotNull(activity);
        assertEquals("", activity.getTitle());
    }

    @Test
    public void test_noStartIntro() {
        assertFalse(activity.startIntro());
    }

    @Test
    public void test_startIntro() {
        activity.setAdapter(new GatewaySpinnerAdapter(activity, new ArrayList<Gateway>()));
        assertTrue(activity.startIntro());
    }

    @Test
    public void test_backPressed_fabExpanded() {
        activity.getFabMenu().expand();
        activity.onBackPressed();

        verify(activity).collapseFab();
    }

    @Test
    public void test_backPressed_fabCollapsed() {
        activity.getFabMenu().collapse();
        activity.onBackPressed();

        verify(activity).onBackPressed();
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
    public void test_expandFab() {
        activity.getFabMenu().expand();
        assertTrue(activity.getFabMenu().isExpanded());

        activity.getAddZone().performClick();
        assertFalse(activity.getFabMenu().isExpanded());
    }

    @Test
    public void test_introResult() {
        activity.onActivityResult(MainActivity.RESULT_INTRO, Activity.RESULT_OK, new Intent());
        verify(activity).recreate();
    }

    @Test
    public void test_introResultCancelled() {
        activity.onActivityResult(MainActivity.RESULT_INTRO, Activity.RESULT_CANCELED, new Intent());
        verify(activity).recreate();
    }

    @Test
    public void test_canceledResult() {
        activity.onActivityResult(2, Activity.RESULT_CANCELED, new Intent());
        verify(activity, times(1)).onActivityResult(eq(2), eq(Activity.RESULT_CANCELED), any(Intent.class));
    }

    @Test
    public void test_gatewayResultOk() {
        activity.onActivityResult(CreationActivity.TYPE_GATEWAY, Activity.RESULT_OK, new Intent());
        verify(activity).setSpinnerAdapter();
    }

    @Test
    public void test_gatewayResultCancelled() {
        activity.onActivityResult(CreationActivity.TYPE_GATEWAY, Activity.RESULT_CANCELED, new Intent());
        verify(activity, times(1)).onActivityResult(eq(CreationActivity.TYPE_GATEWAY), eq(Activity.RESULT_CANCELED), any(Intent.class));
    }

    @Test
    public void test_buttonResultOk() {
        activity.onActivityResult(CreationActivity.TYPE_BUTTON, Activity.RESULT_OK, new Intent());
        verify(activity).setRecycler();
    }

    @Test
    public void test_buttonResultCancelled() {
        activity.onActivityResult(CreationActivity.TYPE_BUTTON, Activity.RESULT_CANCELED, new Intent());
        verify(activity, times(1)).onActivityResult(eq(CreationActivity.TYPE_BUTTON), eq(Activity.RESULT_CANCELED), any(Intent.class));
    }

    @Test
    public void test_zoneResultOk() {
        activity.onActivityResult(CreationActivity.TYPE_ZONE, Activity.RESULT_OK, new Intent());
        verify(activity).setRecycler();
    }

    @Test
    public void test_zoneResultCancelled() {
        activity.onActivityResult(CreationActivity.TYPE_ZONE, Activity.RESULT_CANCELED, new Intent());
        verify(activity, times(1)).onActivityResult(eq(CreationActivity.TYPE_ZONE), eq(Activity.RESULT_CANCELED), any(Intent.class));
    }

    @Test
    public void test_option_settings() {
        MenuItem item = Mockito.mock(MenuItem.class);

        when(item.getItemId()).thenReturn(R.id.settings);
        activity.onOptionsItemSelected(item);
        verify(activity).openOption(any(Class.class));
    }

    @Test
    public void test_option_getHelp() {
        MenuItem item = Mockito.mock(MenuItem.class);

        when(item.getItemId()).thenReturn(R.id.get_help);
        activity.onOptionsItemSelected(item);
        verify(activity).openOption(null);
    }

    @Test
    public void test_option_diagnosis() {
        MenuItem item = Mockito.mock(MenuItem.class);

        when(item.getItemId()).thenReturn(R.id.diagnosis);
        activity.onOptionsItemSelected(item);
        verify(activity).openOption(null);
    }

    @Test
    public void test_option_transferSettings() {
        MenuItem item = Mockito.mock(MenuItem.class);

        when(item.getItemId()).thenReturn(R.id.transfer_settings);
        activity.onOptionsItemSelected(item);
        verify(activity).openOption(null);
    }

    @Test
    public void test_option_none() {
        MenuItem item = Mockito.mock(MenuItem.class);

        when(item.getItemId()).thenReturn(1);
        activity.onOptionsItemSelected(item);
        verify(activity).onOptionsItemSelected(item);
    }

    @Test
    public void test_spinnerSelection() {
        activity = Robolectric.setupActivity(MainActivity.class);
        
        Gateway one = new Gateway();
        one.setId(1);
        one.setName("test 1");

        Gateway two = new Gateway();
        two.setId(2);
        two.setName("test 2");

        gateways.clear();
        gateways.add(one);
        gateways.add(two);

        adapter = new GatewaySpinnerAdapter(activity, gateways);
        activity.setAdapter(adapter);
        activity.getSpinner().setAdapter(adapter);
        activity.getSpinner().setSelection(0);

        activity.getSpinner().getOnItemSelectedListener().onNothingSelected(null);

        activity.getSpinner().setSelection(2);
        assertEquals(0, activity.getCurrentSpinnerSelection());

        activity.getSpinner().setSelection(1);
        assertEquals(1, activity.getCurrentSpinnerSelection());

        activity.getSpinner().setSelection(2);
        assertEquals(1, activity.getCurrentSpinnerSelection());
    }

    private GatewaySpinnerAdapter createAdapter() {
        Gateway one = new Gateway();
        one.setId(1);
        one.setName("test 1");

        Gateway two = new Gateway();
        two.setId(2);
        two.setName("test 2");

        gateways.add(one);
        gateways.add(two);

        return new GatewaySpinnerAdapter(activity, gateways);
    }

}