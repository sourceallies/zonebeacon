/*
 * Copyright (C) 2016 Source Allies, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

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

        when(commandType.getBaseSerialOnCode()).thenReturn("^A%nnn");
        when(commandType.getBaseSerialOffCode()).thenReturn("^B%nnn");
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
        when(commandType.getBaseSerialOnCode()).thenReturn("^a%s%nnn");
        when(commandType.getBaseSerialOffCode()).thenReturn("^b%s%nnn");

        when(command.getNumber()).thenReturn(5);
        when(command.getControllerNumber()).thenReturn(1);
        assertEquals("^a1005", interpreter.getExecutable(command, Executor.LoadStatus.OFF));
        assertEquals("^b1005", interpreter.getExecutable(command, Executor.LoadStatus.ON));
    }

    @Test
    public void test_getExecutable_brightness() {
        when(commandType.getBaseSerialOnCode()).thenReturn("^E%nnn%ll00");
        when(commandType.getBaseSerialOffCode()).thenReturn("^E%nnn%ll00");

        when(command.getNumber()).thenReturn(5);
        assertEquals("^E0058000", interpreter.getExecutable(command, 80, Executor.LoadStatus.OFF));
        assertEquals("^E0058000", interpreter.getExecutable(command, 80, Executor.LoadStatus.ON));
    }

    @Test
    public void test_processResponse() {
        assertEquals("12345", interpreter.processResponse("12345"));
    }

    @Test
    public void test_addZeros() {
        assertEquals("001", interpreter.addZeros("1", 3));
        assertEquals("1", interpreter.addZeros("1", 1));
    }

    @Test
    public void test_buildQueryCommand() {
        Command command = interpreter.buildQueryActiveLoadsCommand();
        assertEquals("Query Active Loads", command.getName());
        assertEquals(0, (int) command.getControllerNumber());
        assertEquals(0, command.getNumber());
        assertEquals(-1, command.getId());
        assertEquals(-1, command.getGatewayId());
    }

    @Test
    public void test_queryActiveLoadsCommandString() {
        assertEquals("^G", interpreter.getQueryActiveLoadsCommandString());
    }

    @Test
    public void test_processActiveLoads() {
        assertEquals(0, interpreter.processActiveLoadsResponse("test text").size());
    }
}