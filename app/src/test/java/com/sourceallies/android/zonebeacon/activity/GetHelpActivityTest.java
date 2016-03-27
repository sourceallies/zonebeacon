package com.sourceallies.android.zonebeacon.activity;

import android.content.Intent;

import com.sourceallies.android.zonebeacon.ZoneBeaconRobolectricSuite;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.robolectric.Robolectric;

import static org.junit.Assert.assertNotNull;

public class GetHelpActivityTest extends ZoneBeaconRobolectricSuite {

    private GetHelpActivity activity;

    @Before
    public void setUp() {
        activity = Mockito.spy(Robolectric.setupActivity(GetHelpActivity.class));
    }

    @Test
    public void test_notNull() {
        assertNotNull(activity);
    }

    @Test
    public void test_videoLink() {
        activity.getClickListener(activity).onClick("this YouTube channel");
        Mockito.verify(activity).startActivity(Mockito.any(Intent.class));
    }
}
