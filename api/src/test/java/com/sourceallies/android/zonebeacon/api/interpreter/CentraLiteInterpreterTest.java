package com.sourceallies.android.zonebeacon.api.interpreter;

import com.sourceallies.android.zonebeacon.ZoneBeaconSuite;
import com.sourceallies.android.zonebeacon.api.executor.Executor;
import com.sourceallies.android.zonebeacon.api.interpreter.CentraLiteInterpreter;
import com.sourceallies.android.zonebeacon.data.model.Command;
import com.sourceallies.android.zonebeacon.data.model.CommandType;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import static org.junit.Assert.*;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

public class CentraLiteInterpreterTest extends ZoneBeaconSuite {

    private CentraLiteInterpreter interpreter;

    @Mock
    private Command command;
    @Mock
    private CommandType commandType;

    @Before
    public void setUp() {
        interpreter = new CentraLiteInterpreter();

        when(commandType.getBaseSerialOnCode()).thenReturn("^A");
        when(commandType.getBaseSerialOffCode()).thenReturn("^B");
        when(command.getCommandType()).thenReturn(commandType);
    }

    @Test
    public void test_notNull() {
        assertNotNull(interpreter);
    }

    @Test
    public void test_getExecutable_noZeros() {
        when(command.getNumber()).thenReturn(150);
        when(command.getControllerNumber()).thenReturn(null);
        assertEquals("^A150", interpreter.getExecutable(command, Executor.LoadStatus.OFF));
        assertEquals("^B150", interpreter.getExecutable(command, Executor.LoadStatus.ON));
    }

    @Test
    public void test_getExecutable_oneZero() {
        when(command.getNumber()).thenReturn(21);
        when(command.getControllerNumber()).thenReturn(null);
        assertEquals("^A021", interpreter.getExecutable(command, Executor.LoadStatus.OFF));
        assertEquals("^B021", interpreter.getExecutable(command, Executor.LoadStatus.ON));
    }

    @Test
    public void test_getExecutable_twoZeros() {
        when(command.getNumber()).thenReturn(5);
        when(command.getControllerNumber()).thenReturn(null);
        assertEquals("^A005", interpreter.getExecutable(command, Executor.LoadStatus.OFF));
        assertEquals("^B005", interpreter.getExecutable(command, Executor.LoadStatus.ON));
    }

    @Test
    public void test_getExecutable_controller() {
        when(command.getNumber()).thenReturn(5);
        when(command.getControllerNumber()).thenReturn(1);
        assertEquals("^A1005", interpreter.getExecutable(command, Executor.LoadStatus.OFF));
        assertEquals("^B1005", interpreter.getExecutable(command, Executor.LoadStatus.ON));
    }

    @Test
    public void test_processResponse() {
        assertEquals("12345", interpreter.processResponse("12345"));
    }

}