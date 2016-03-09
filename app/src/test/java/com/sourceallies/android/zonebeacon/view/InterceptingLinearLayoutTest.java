package com.sourceallies.android.zonebeacon.view;

import com.sourceallies.android.zonebeacon.ZoneBeaconSuite;

import org.junit.Test;

import static org.junit.Assert.*;

public class InterceptingLinearLayoutTest extends ZoneBeaconSuite {

    @Test
    public void test_interceptsAllTouches() {
        // go through both constructors to verify no exceptions thrown.
        InterceptingLinearLayout layout = new InterceptingLinearLayout(null);
        layout = new InterceptingLinearLayout(null, null);

        assertTrue(layout.onInterceptTouchEvent(null));
    }

}