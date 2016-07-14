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
import android.view.MenuItem;

import com.sourceallies.android.zonebeacon.R;
import com.sourceallies.android.zonebeacon.ZoneBeaconRobolectricSuite;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.robolectric.Robolectric;

import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class GetHelpActivityTest extends ZoneBeaconRobolectricSuite {

    private GetHelpActivity activity;

    @Before
    public void setUp() {
        activity = Mockito.spy(Robolectric.setupActivity(GetHelpActivity.class));

        setActivityToBeTornDown(activity);
    }

    @Test
    public void test_notNull() {
        assertNotNull(activity);
        assertNotNull(activity.getVideoLink());
    }

    @Test
    public void test_videoLink() {
        activity.getClickListener(activity).onClick("this YouTube channel");
        Mockito.verify(activity).startActivity(Mockito.any(Intent.class));
    }

    @Test
    public void test_option_home() {
        MenuItem item = Mockito.mock(MenuItem.class);

        when(item.getItemId()).thenReturn(android.R.id.home);
        activity.onOptionsItemSelected(item);
        verify(activity).finish();
    }

    @Test
    public void test_option_default() {
        MenuItem item = Mockito.mock(MenuItem.class);

        when(item.getItemId()).thenReturn(R.id.diagnosis);
        activity.onOptionsItemSelected(item);
        verify(activity).onOptionsItemSelected(item);
    }
}
