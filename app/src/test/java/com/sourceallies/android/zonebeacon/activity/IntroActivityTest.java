package com.sourceallies.android.zonebeacon.activity;

import com.sourceallies.android.zonebeacon.ZoneBeaconRobolectricSuite;

import org.junit.Before;
import org.junit.Test;
import org.robolectric.Robolectric;

import static org.junit.Assert.*;

public class IntroActivityTest extends ZoneBeaconRobolectricSuite {

    private IntroActivity activity;

    @Before
    public void setUp() {
        activity = Robolectric.setupActivity(IntroActivity.class);
    }

    @Test
    public void test_notNull() {
        assertNotNull(activity);
    }

    @Test
    public void test_twoFragments() {
        assertEquals(2, activity.getViewPager().getAdapter().getCount());
    }

}