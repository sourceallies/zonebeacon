package com.sourceallies.android.zonebeacon.api;

import com.sourceallies.android.zonebeacon.ZoneBeaconSuite;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class CentraLiteInterpreterTest extends ZoneBeaconSuite {

    private CentraLiteInterpreter interpreter;

    @Before
    public void setUp() {
        interpreter = new CentraLiteInterpreter();
    }

    @Test
    public void test_notNull() {
        assertNotNull(interpreter);
    }

}