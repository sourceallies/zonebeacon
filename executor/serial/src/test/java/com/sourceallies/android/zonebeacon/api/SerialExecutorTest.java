package com.sourceallies.android.zonebeacon.api;

import com.sourceallies.android.zonebeacon.ZoneBeaconSuite;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import static org.junit.Assert.*;

public class SerialExecutorTest extends ZoneBeaconSuite {

    private SerialExecutor executor;

    @Mock
    private Interpreter interpreter;

    @Before
    public void setUp() {
        executor = new SerialExecutor(interpreter);
    }

    @Test
    public void test_notNull() {
        assertNotNull(executor);
    }

    // TODO more tests needed

}