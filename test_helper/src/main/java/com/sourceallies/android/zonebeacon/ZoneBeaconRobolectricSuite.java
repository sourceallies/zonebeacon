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

package com.sourceallies.android.zonebeacon;

import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import org.junit.After;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.annotation.Config;

import java.lang.reflect.Method;

import roboguice.activity.RoboFragmentActivity;

/**
 * Superclass for running tests with Robolectric. These will be slower than normal unit tests.
 */
@RunWith(RobolectricGradleTestRunner.class)
@Config(sdk = 21, constants = BuildConfig.class)
public abstract class ZoneBeaconRobolectricSuite {

    @Before
    public final void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @After
    public final void teardown() {
        try {
            Class clazz = Class.forName("com.sourceallies.android.zonebeacon.data.DataSource");
            Method method = clazz.getMethod("forceCloseImmediate");
            method.invoke(null);
        } catch (Throwable e) {
        }
    }

    /**
     * Helper for starting a fragment inside a FragmentActivity.
     *
     * @param fragment the fragment to start.
     */
    public static Fragment startFragment(Fragment fragment) {
        FragmentActivity activity = Robolectric.buildActivity(RoboFragmentActivity.class)
                .create()
                .start()
                .get();

        FragmentManager fragmentManager = activity.getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(fragment, null);
        fragmentTransaction.commit();

        return fragment;
    }

    /**
     * Helper for displaying a dialog fragment.
     *
     * @param fragment the fragment to display.
     * @return the fragment.
     */
    public static Fragment startDialogFragment(DialogFragment fragment) {
        FragmentActivity activity = Robolectric.buildActivity(RoboFragmentActivity.class)
                .create()
                .start()
                .get();

        FragmentManager fragmentManager = activity.getSupportFragmentManager();
        fragment.show(fragmentManager, "fragment");

        return fragment;
    }

    /**
     * Helper for starting a fragment inside a FragmentActivity.
     *
     * @param fragment the fragment to start.
     */
    public static android.app.Fragment startFragment(android.app.Fragment fragment) {
        FragmentActivity activity = Robolectric.buildActivity(RoboFragmentActivity.class)
                .create()
                .start()
                .get();

        android.app.FragmentManager fragmentManager = activity.getFragmentManager();
        android.app.FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(fragment, null);
        fragmentTransaction.commit();

        return fragment;
    }

}
