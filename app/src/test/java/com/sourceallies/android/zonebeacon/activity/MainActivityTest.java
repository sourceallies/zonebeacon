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

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;

import com.getbase.floatingactionbutton.FloatingActionButton;
import com.sourceallies.android.zonebeacon.R;
import com.sourceallies.android.zonebeacon.ZoneBeaconRobolectricSuite;
import com.sourceallies.android.zonebeacon.adapter.GatewaySpinnerAdapter;
import com.sourceallies.android.zonebeacon.api.QueryLoadsCallback;
import com.sourceallies.android.zonebeacon.api.executor.Executor;
import com.sourceallies.android.zonebeacon.data.DataSource;
import com.sourceallies.android.zonebeacon.data.model.Gateway;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.robolectric.Robolectric;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyList;
import static org.mockito.Matchers.anyMap;
import static org.mockito.Matchers.eq;
import static org.mockito.Matchers.isNull;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.robolectric.Shadows.shadowOf;

public class MainActivityTest extends ZoneBeaconRobolectricSuite {

    @Mock
    private DataSource dataSource;
    @Mock
    private Executor executor;
    @Mock
    private SharedPreferences sharedPrefs;
    @Mock
    private Gateway gateway;

    @Mock
    private RecyclerView recycler;

    private MainActivity activity;
    private List<Gateway> gateways = new ArrayList<>();
    private GatewaySpinnerAdapter adapter;

    @Before
    public void setUp() {
        activity = Robolectric.setupActivity(MainActivity.class);
        activity = spy(activity);

        doNothing().when(activity).recreate();
        doNothing().when(executor).queryActiveLoads(any(Gateway.class), any(QueryLoadsCallback.class));
        doReturn(new ArrayList()).when(dataSource).findButtons(any(Gateway.class));
        doReturn(new ArrayList()).when(dataSource).findZones(any(Gateway.class));

        doNothing().when(recycler).setLayoutManager(any(RecyclerView.LayoutManager.class));
        doReturn(executor).when(activity).getExecutor();
        doReturn(dataSource).when(activity).getDataSource();

        adapter = createAdapter();
        activity.setSpinnerAdapter(adapter);
        activity.getSpinner().setAdapter(adapter);
        activity.setRecycler(recycler);

        setActivityToBeTornDown(activity);
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
        activity.setSpinnerAdapter(new GatewaySpinnerAdapter(activity, new ArrayList<Gateway>()));
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
        assertNotNull(activity.getGetHelp());
        assertNotNull(activity.getAddZone());
        assertNotNull(activity.getAddButton());
        assertNotNull(activity.getAddCommand());
        assertNotNull(activity.getSpinner());
        assertNotNull(activity.getDim());
        assertNotNull(activity.getRecycler());
        assertNotNull(activity.getSwipeRefreshLayout());
    }

    @Test
    public void test_noAdapter() {
        assertNull(activity.getMainAdapter());
    }

    @Test
    public void test_dimListener() {
        assertTrue(activity.getDimListener().onTouch(null, null));
    }

    @Test
    public void test_dimShown() {
        activity.getFabMenu().expand();
        assertTrue(activity.getDim().getVisibility() == View.VISIBLE);

        activity.getFabMenu().collapse();
        assertTrue(activity.getDim().getVisibility() == View.GONE);
    }

    @Test
    public void test_expandFab_clickZone() {
        test_spinnerSelection();
        activity = spy(activity);

        activity.getFabMenu().expand();
        assertTrue(activity.getFabMenu().isExpanded());

        activity.getAddZone().performClick();
        assertFalse(activity.getFabMenu().isExpanded());
    }

    @Test
    public void test_expandFab_clickButton() {
        test_spinnerSelection();
        activity = spy(activity);

        activity.getFabMenu().expand();
        assertTrue(activity.getFabMenu().isExpanded());

        activity.getAddButton().performClick();
        assertFalse(activity.getFabMenu().isExpanded());
    }

    @Test
    public void test_expandFab_clickCommand() {
        test_spinnerSelection();
        activity = spy(activity);

        activity.getFabMenu().expand();
        assertTrue(activity.getFabMenu().isExpanded());

        activity.getAddCommand().performClick();
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
    public void test_getHelp() {
        activity.getHelpListener().onClick(null);
        verify(activity).collapseFab();
        verify(activity).openOption(GetHelpActivity.class);
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
        verify(activity).openOption(TransferActivity.class);
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
        activity.setSpinnerAdapter(adapter);
        activity.getSpinner().setAdapter(adapter);
        activity.setRecycler(recycler);
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

    @Test
    public void test_columnCount() {
        Resources res = Mockito.mock(Resources.class);
        Configuration config = Mockito.mock(Configuration.class);
        doReturn(res).when(activity).getResources();
        doReturn(config).when(res).getConfiguration();

        config.orientation = Configuration.ORIENTATION_LANDSCAPE;
        assertEquals(2, activity.getColumnCount());

        config.orientation = Configuration.ORIENTATION_PORTRAIT;
        doReturn(true).when(res).getBoolean(R.bool.tablet);
        assertEquals(2, activity.getColumnCount());

        config.orientation = Configuration.ORIENTATION_PORTRAIT;
        doReturn(false).when(res).getBoolean(R.bool.tablet);
        assertEquals(1, activity.getColumnCount());
    }

    @Test
    public void test_queryCallback() {
        Runnable runnable = Mockito.mock(Runnable.class);
        doReturn(runnable).when(activity).setLoadStatusRunnable(any(Gateway.class), anyList(), anyList(), anyMap());
        activity.getQueryCallback(activity, Mockito.mock(Gateway.class), Mockito.mock(List.class), Mockito.mock(List.class))
            .onResponse(Mockito.mock(Map.class));

        verify(activity).runOnUiThread(runnable);
    }

    @Test
    public void test_setLoadRunnable() {
        doNothing().when(activity).loadOnOffStatusesToAdapter(anyList(), anyList(), anyMap());
        activity.setLoadStatusRunnable(Mockito.mock(Gateway.class), Mockito.mock(List.class), Mockito.mock(List.class), Mockito.mock(Map.class))
                .run();

        verify(activity).loadOnOffStatusesToAdapter(anyList(), anyList(), anyMap());
    }

    @Test
    public void test_swipeRefreshListener() {
        doNothing().when(activity).setRecycler();
        activity.getRefreshListener(activity).onRefresh();

        verify(activity).setRecycler();
    }

    @Test
    public void test_justCreated() {
        activity.setJustCreated(true);
        activity.onResume();

        verify(activity, never()).setRecycler();
        assertFalse(activity.isJustCreated());
    }
}