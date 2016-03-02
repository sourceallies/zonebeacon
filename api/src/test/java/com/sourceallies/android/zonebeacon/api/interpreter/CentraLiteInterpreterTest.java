package com.sourceallies.android.zonebeacon.api.executor;

import com.sourceallies.android.zonebeacon.ZoneBeaconSuite;
import com.sourceallies.android.zonebeacon.api.interpreter.CentraLiteInterpreter;
import com.sourceallies.android.zonebeacon.data.model.Command;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import static org.junit.Assert.*;
import static org.mockito.Mockito.when;

public class CentraLiteInterpreterTest extends ZoneBeaconSuite {

    private CentraLiteInterpreter interpreter;

    @Mock
    private Command command;

    @Before
    public void setUp() {
        interpreter = new CentraLiteInterpreter();
    }

    @Test
    public void test_notNull() {
        assertNotNull(interpreter);
    }

    @Test
    public void test_getExecutable_noZeros() {
        when(command.getNumber()).thenReturn(150);
        assertEquals("^a1150", interpreter.getExecutable(command));
    }

    @Test
    public void test_getExecutable_oneZero() {
        when(command.getNumber()).thenReturn(21);
        assertEquals("^a1021", interpreter.getExecutable(command));
    }

    @Test
    public void test_getExecutable_twoZeros() {
        when(command.getNumber()).thenReturn(5);
        assertEquals("^a1005", interpreter.getExecutable(command));
    }

    @Test
    public void test_processResponse() {
        assertEquals("12345", interpreter.processResponse("12345"));
    }

}