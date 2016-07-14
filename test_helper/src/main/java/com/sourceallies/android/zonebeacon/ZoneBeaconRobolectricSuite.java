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

import android.annotation.SuppressLint;
import android.app.Activity;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.ArraySet;
import android.view.View;

import org.junit.After;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.annotation.Config;
import org.robolectric.util.ReflectionHelpers;

import java.lang.reflect.Method;
import java.util.ArrayList;

import roboguice.activity.RoboFragmentActivity;

/**
 * Superclass for running tests with Robolectric. These will be slower than normal unit tests.
 */
@RunWith(RobolectricGradleTestRunner.class)
@Config(sdk = 21, constants = BuildConfig.class)
public abstract class ZoneBeaconRobolectricSuite {

    private Activity activity;

    protected void setActivityToBeTornDown(Activity activityToBeTornDown) {
        this.activity = activityToBeTornDown;
    }

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
        } catch (Throwable e) { }

        if (activity != null) {
            activity.finish();
        }
    }

    // to counter heap space errors: https://github.com/robolectric/robolectric/issues/2068
    @SuppressLint("NewApi")
    @After
    public void resetWindowManager() throws Exception {
        // https://github.com/robolectric/robolectric/pull/1741
        final Class<?> btclass = Class.forName("com.android.internal.os.BackgroundThread");
        Object backgroundThreadSingleton = ReflectionHelpers.getStaticField(btclass,"sInstance");
        if (backgroundThreadSingleton!=null) {
            btclass.getMethod("quit").invoke(backgroundThreadSingleton);
            ReflectionHelpers.setStaticField(btclass, "sInstance", null);
            ReflectionHelpers.setStaticField(btclass, "sHandler", null);
        }

        // https://github.com/robolectric/robolectric/issues/2068
        Class clazz = ReflectionHelpers.loadClass(getClass().getClassLoader(), "android.view.WindowManagerGlobal");
        Object instance = ReflectionHelpers.callStaticMethod(clazz, "getInstance");

        // We essentially duplicate what's in {@link WindowManagerGlobal#closeAll} with what's below.
        // The closeAll method has a bit of a bug where it's iterating through the "roots" but
        // bases the number of objects to iterate through by the number of "views." This can result in
        // an {@link java.lang.IndexOutOfBoundsException} being thrown.
        Object lock = ReflectionHelpers.getField(instance, "mLock");

        ArrayList<Object> roots = ReflectionHelpers.getField(instance, "mRoots");
        //noinspection SynchronizationOnLocalVariableOrMethodParameter
        synchronized (lock) {
            for (int i = 0; i < roots.size(); i++) {
                ReflectionHelpers.callInstanceMethod(instance, "removeViewLocked",
                        ReflectionHelpers.ClassParameter.from(int.class, i),
                        ReflectionHelpers.ClassParameter.from(boolean.class, false));
            }
        }

        // Views will still be held by this array. We need to clear it out to ensure
        // everything is released.
        ArraySet<View> dyingViews = ReflectionHelpers.getField(instance, "mDyingViews");
        dyingViews.clear();
    }

    /**
     * Helper for starting a fragment inside a FragmentActivity.
     *
     * @param fragment the fragment to start.
     */
    public Fragment startFragment(Fragment fragment) {
        FragmentActivity activity = Robolectric.buildActivity(RoboFragmentActivity.class)
                .create()
                .start()
                .get();

        FragmentManager fragmentManager = activity.getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(fragment, null);
        fragmentTransaction.commit();

        setActivityToBeTornDown(activity);

        return fragment;
    }

    /**
     * Helper for displaying a dialog fragment.
     *
     * @param fragment the fragment to display.
     * @return the fragment.
     */
    public Fragment startDialogFragment(DialogFragment fragment) {
        FragmentActivity activity = Robolectric.buildActivity(RoboFragmentActivity.class)
                .create()
                .start()
                .get();

        FragmentManager fragmentManager = activity.getSupportFragmentManager();
        fragment.show(fragmentManager, "fragment");

        setActivityToBeTornDown(activity);

        return fragment;
    }

    /**
     * Helper for starting a fragment inside a FragmentActivity.
     *
     * @param fragment the fragment to start.
     */
    public android.app.Fragment startFragment(android.app.Fragment fragment) {
        FragmentActivity activity = Robolectric.buildActivity(RoboFragmentActivity.class)
                .create()
                .start()
                .get();

        android.app.FragmentManager fragmentManager = activity.getFragmentManager();
        android.app.FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(fragment, null);
        fragmentTransaction.commit();

        setActivityToBeTornDown(activity);

        return fragment;
    }

}
