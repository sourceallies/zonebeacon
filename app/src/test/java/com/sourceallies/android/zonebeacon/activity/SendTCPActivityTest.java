package com.sourceallies.android.zonebeacon.activity;

import com.sourceallies.android.zonebeacon.ZoneBeaconRobolectricSuite;

import org.junit.Before;
import org.junit.Test;
import org.robolectric.Robolectric;

import static org.junit.Assert.*;

public class SendTCPActivityTest extends ZoneBeaconRobolectricSuite{

    private SendTCPActivity activity;

    @Before
    public void setUp() {
        activity = Robolectric.setupActivity(SendTCPActivity.class);
    }

    @Test
    public void test_notNull() {
        assertNotNull(activity);
    }
}