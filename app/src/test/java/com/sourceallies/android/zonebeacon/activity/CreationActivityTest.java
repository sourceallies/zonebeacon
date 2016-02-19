package com.sourceallies.android.zonebeacon.activity;

import com.sourceallies.android.zonebeacon.ZoneBeaconRobolectricSuite;
import com.sourceallies.android.zonebeacon.fragment.GatewaySetupFragment;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.robolectric.Robolectric;

import static org.junit.Assert.*;

public class CreationActivityTest extends ZoneBeaconRobolectricSuite {

    private CreationActivity activity;

    @Before
    public void setUp() {
        activity = Robolectric.setupActivity(CreationActivity.class);
        activity = Mockito.spy(activity);
    }

    @Test
    public void test_addGateway() {
        Mockito.doReturn(CreationActivity.TYPE_GATEWAY).when(activity).getFragmentType();
        assertTrue(activity.getSetupFragment() instanceof GatewaySetupFragment);
    }

    @Test
    public void test_addZone() {
        // TODO: change the instance of after new fragments are added
        Mockito.doReturn(CreationActivity.TYPE_ZONE).when(activity).getFragmentType();
        assertTrue(activity.getSetupFragment() instanceof GatewaySetupFragment);
    }

    @Test
    public void test_addButton() {
        // TODO: change the instance of after new fragments are added
        Mockito.doReturn(CreationActivity.TYPE_BUTTON).when(activity).getFragmentType();
        assertTrue(activity.getSetupFragment() instanceof GatewaySetupFragment);
    }

    @Test
    public void test_addCommand() {
        // TODO: change the instance of after new fragments are added
        Mockito.doReturn(CreationActivity.TYPE_COMMAND).when(activity).getFragmentType();
        assertTrue(activity.getSetupFragment() instanceof GatewaySetupFragment);
    }
}