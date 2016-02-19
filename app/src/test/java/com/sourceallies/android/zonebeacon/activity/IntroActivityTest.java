package com.sourceallies.android.zonebeacon.activity;

import com.sourceallies.android.zonebeacon.ZoneBeaconRobolectricSuite;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
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
        activity.onNextPressed();
    }

    @Test
    public void test_twoFragments() {
        assertEquals(2, activity.getViewPager().getAdapter().getCount());
    }

    @Test
    public void test_formFilled() {
        IntroActivity spy = Mockito.spy(activity);

        spy.getSetupFragment().getName().getEditText().setText("Test Gateway");
        spy.getSetupFragment().getIpAddress().getEditText().setText("192.168.1.100");
        spy.getSetupFragment().getPort().getEditText().setText("11000");

        spy.onDonePressed();

        Mockito.verify(spy, Mockito.times(1)).saveGateway();
    }

    @Test
    public void test_formError() {
        IntroActivity spy = Mockito.spy(activity);

        spy.getSetupFragment().getName().getEditText().setText("");
        spy.getSetupFragment().getIpAddress().getEditText().setText("192.168.1.100");
        spy.getSetupFragment().getPort().getEditText().setText("11000");

        spy.onDonePressed();

        Mockito.verify(spy, Mockito.times(0)).saveGateway();
    }

}