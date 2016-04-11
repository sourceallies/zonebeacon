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

package com.sourceallies.android.zonebeacon.view;

import android.app.Activity;
import android.graphics.Canvas;
import android.graphics.Paint;

import com.sourceallies.android.zonebeacon.ZoneBeaconRobolectricSuite;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.robolectric.Robolectric;
import org.robolectric.annotation.Config;

import static org.junit.Assert.*;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyFloat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

public class NearbyBackgroundLayoutTest extends ZoneBeaconRobolectricSuite {

    private NearbyBackgroundLayout layout;

    @Mock
    private Canvas canvas;

    @Before
    public void setUp() {
        Activity activity = Robolectric.setupActivity(Activity.class);
        layout = new NearbyBackgroundLayout(activity);
        activity.setContentView(layout);
    }

    @Test
    public void test_onDraw() {
        layout.onDraw(canvas);
        verify(canvas, times(3)).drawCircle(anyFloat(), anyFloat(), anyFloat(), any(Paint.class));
    }

}