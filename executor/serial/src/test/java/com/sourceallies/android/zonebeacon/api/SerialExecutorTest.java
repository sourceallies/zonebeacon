package com.sourceallies.android.zonebeacon.api;

import com.sourceallies.android.zonebeacon.ZoneBeaconSuite;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class SerialExecutorTest extends ZoneBeaconSuite {

    private SerialExecutor executor;

    @Before
    public void setUp() {
        executor = new SerialExecutor();
    }

    @Test
    public void test_notNull() {
        assertNotNull(executor);
    }

}