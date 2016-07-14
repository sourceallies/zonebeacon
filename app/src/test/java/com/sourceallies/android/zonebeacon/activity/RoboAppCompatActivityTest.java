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

import android.content.Intent;
import android.content.res.Configuration;
import android.support.v4.app.TaskStackBuilder;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatDelegate;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.sourceallies.android.zonebeacon.R;
import com.sourceallies.android.zonebeacon.ZoneBeaconRobolectricSuite;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.robolectric.Robolectric;

import static org.junit.Assert.*;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

public class RoboAppCompatActivityTest extends ZoneBeaconRobolectricSuite {

    private RoboAppCompatActivity activity;

    @Mock
    private AppCompatDelegate delegate;
    @Mock
    private Configuration configuration;
    @Mock
    private View view;
    @Mock
    private ViewGroup.LayoutParams layoutParams;
    @Mock
    private Toolbar toolbar;
    @Mock
    private ActionBar actionBar;
    @Mock
    private MenuItem menuItem;
    @Mock
    private TaskStackBuilder taskStackBuilder;
    @Mock
    private ActionBarDrawerToggle.Delegate actionBarDrawerToggleDelegate;
    @Mock
    private Menu menu;

    @Before
    public void setUp() {
        activity = Robolectric.setupActivity(RoboAppCompatActivity.class);
        setActivityToBeTornDown(activity);
    }

    @Test
    public void test_created() {
        assertNotNull(activity);
    }

    @Test
    public void test_getSupportActionBar() {
        activity.setDelegate(delegate);
        when(delegate.getSupportActionBar()).thenReturn(actionBar);
        assertNotNull(activity.getSupportActionBar());
    }

    @Test
    public void test_setSupportActionBar() {
        activity.setDelegate(delegate);
        activity.setSupportActionBar(toolbar);
        verify(delegate).setSupportActionBar(toolbar);
    }

    @Test
    public void test_getMenuInflater() {
        assertNotNull(activity.getMenuInflater());
    }

    @Test
    public void test_setContentView_withLayout() {
        activity.setDelegate(delegate);
        activity.setContentView(R.layout.fragment_add);
        verify(delegate).setContentView(R.layout.fragment_add);
    }

    @Test
    public void test_setContentView_withView() {
        activity.setDelegate(delegate);
        activity.setContentView(view);
        verify(delegate).setContentView(view);

        activity.setContentView(view, layoutParams);
        verify(delegate).setContentView(view, layoutParams);
    }

    @Test
    public void test_addContentView() {
        activity.setDelegate(delegate);
        activity.addContentView(view, layoutParams);
        verify(delegate).addContentView(view, layoutParams);
    }

    @Test
    public void test_onConfigurationChanged() {
        activity.setDelegate(delegate);
        activity.onConfigurationChanged(configuration);
        verify(delegate).onConfigurationChanged(configuration);
    }

    @Test
    public void test_onStop() {
        activity.setDelegate(delegate);
        activity.onStop();
        verify(delegate).onStop();
    }

    @Test
    public void test_onMenuItemSelected() {
        activity.onMenuItemSelected(1, menuItem);
    }

    @Test
    public void test_onDestroy() {
        activity.setDelegate(delegate);
        activity.onDestroy();
        verify(delegate).onDestroy();
    }

    @Test
    public void test_supportRequestWindowFeature() {
        activity.setDelegate(delegate);
        activity.supportRequestWindowFeature(0);
        verify(delegate).requestWindowFeature(0);
    }

    @Test
    public void test_supportInvalidateOptionsMenu() {
        activity.setDelegate(delegate);
        activity.supportInvalidateOptionsMenu();
        verify(delegate).invalidateOptionsMenu();
    }

    @Test
    public void test_invalidateOptionsMenu() {
        activity.setDelegate(delegate);
        activity.invalidateOptionsMenu();
        verify(delegate).invalidateOptionsMenu();
    }

    @Test
    public void test_onSupportActionModeStarted() {
        activity.onSupportActionModeStarted(null);
    }

    @Test
    public void test_onSupportActionModeFinished() {
        activity.onSupportActionModeFinished(null);
    }

    @Test
    public void test_onWindowStartingSupportActionMode() {
        assertNull(activity.onWindowStartingSupportActionMode(null));
    }

    @Test
    public void test_startSupportActionMode() {
        activity.setDelegate(delegate);
        activity.startSupportActionMode(null);
        verify(delegate).startSupportActionMode(null);
    }

    @Test
    public void test_onCreateSupportNavigateUpTaskStack() {
        activity.onCreateSupportNavigateUpTaskStack(taskStackBuilder);
        verify(taskStackBuilder).addParentStack(activity);
    }

    @Test
    public void test_onPrepareSupportNavigateUpTaskStack() {
        activity.onPrepareSupportNavigateUpTaskStack(taskStackBuilder);
        verifyNoMoreInteractions(taskStackBuilder);
    }

    @Test
    public void test_onSupportNavigateUp() {
        activity.onSupportNavigateUp();
    }

    @Test
    public void test_getSupportParentActivityIntent() {
        assertNull(activity.getSupportParentActivityIntent());
    }

    @Test
    public void test_onContentChanged() {
        activity.onContentChanged();
    }

    @Test
    public void test_onSupportContentChanged() {
        activity.onSupportContentChanged();
    }

    @Test
    public void test_getDrawerToggleDelegate() {
        activity.setDelegate(delegate);
        when(delegate.getDrawerToggleDelegate()).thenReturn(actionBarDrawerToggleDelegate);
        assertEquals(actionBarDrawerToggleDelegate, activity.getDrawerToggleDelegate());
    }

    @Test
    public void test_onMenuOpened() {
        activity.onMenuOpened(0, menu);
    }

    @Test
    public void test_onPanelClosed() {
        activity.onPanelClosed(0, menu);
    }

    @Test
    public void test_superMenuItemSelected() {
        activity = spy(activity);

        doReturn(true).when(activity).superOnMenuItemSelected(anyInt(), any(MenuItem.class));
        assertTrue(activity.onMenuItemSelected(1, Mockito.mock(MenuItem.class)));
    }

    @Test
    public void test_menuItem_navUp() {
        activity = spy(activity);

        doReturn(true).when(activity).supportNavUp(any(MenuItem.class));
        doReturn(true).when(activity).onSupportNavigateUp();
        assertTrue(activity.onMenuItemSelected(1, Mockito.mock(MenuItem.class)));

        doReturn(false).when(activity).onSupportNavigateUp();
        assertFalse(activity.onMenuItemSelected(1, Mockito.mock(MenuItem.class)));
    }

    @Test
    public void test_supportNavUpTo() {
        assertFalse(activity.supportShouldUpRecreateTask(new Intent(activity, RoboAppCompatActivity.class)));
    }

    @Test
    public void test_stackBuilder() {
        assertNotNull(activity.getStackBuilder());
    }

    @Test
    public void test_upIntentNotNull_recreateTask() {
        activity = spy(activity);
        TaskStackBuilder builder = Mockito.mock(TaskStackBuilder.class);

        doReturn(new Intent(activity, RoboAppCompatActivity.class)).when(activity).getSupportParentActivityIntent();
        doReturn(true).when(activity).supportShouldUpRecreateTask(any(Intent.class));
        doNothing().when(builder).startActivities();
        doReturn(builder).when(activity).getStackBuilder();

        assertTrue(activity.onSupportNavigateUp());
        verify(activity).finish();
    }

    @Test
    public void test_upIntentNotNull_noRecreate() {
        activity = spy(activity);

        doReturn(new Intent(activity, RoboAppCompatActivity.class)).when(activity).getSupportParentActivityIntent();
        doReturn(false).when(activity).supportShouldUpRecreateTask(any(Intent.class));

        assertTrue(activity.onSupportNavigateUp());
        verify(activity).supportNavigateUpTo(any(Intent.class));
    }
}