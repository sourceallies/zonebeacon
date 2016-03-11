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

package com.sourceallies.android.zonebeacon.util;

import android.support.design.widget.Snackbar;
import android.widget.LinearLayout;

import com.sourceallies.android.zonebeacon.ZoneBeaconRobolectricSuite;
import com.sourceallies.android.zonebeacon.ZoneBeaconSuite;
import com.sourceallies.android.zonebeacon.activity.MainActivity;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.robolectric.Robolectric;

import static org.junit.Assert.*;

public class FloatingActionButtonBehaviorTest extends ZoneBeaconSuite {

    FloatingActionButtonBehavior behavior;

    @Mock
    private Snackbar.SnackbarLayout snackbarLayout;
    @Mock
    private LinearLayout linearLayout;

    @Before
    public void setUp() {
        behavior = new FloatingActionButtonBehavior(null, null);
    }

    @Test
    public void test_dependency() {
        assertTrue(behavior.layoutDependsOn(null, null, snackbarLayout));
        assertFalse(behavior.layoutDependsOn(null, null, linearLayout));
    }

    @Test
    public void test_translation() {
        Mockito.when(snackbarLayout.getTranslationY()).thenReturn(0f);
        Mockito.when(snackbarLayout.getHeight()).thenReturn(0);

        behavior.onDependentViewChanged(null, linearLayout, snackbarLayout);

        assertEquals(0, linearLayout.getTranslationY(), .1f);
    }
}
