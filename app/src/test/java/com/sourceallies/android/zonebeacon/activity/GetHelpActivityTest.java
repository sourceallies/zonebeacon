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
