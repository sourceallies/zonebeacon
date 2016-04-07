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

import java.util.Iterator;
import java.util.List;
import java.util.Map;

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
        Command command = interpreter.buildQueryActiveLoadsCommand(-1);
        assertEquals("Query Active Loads", command.getName());
        assertEquals(Interpreter.SINGLE_MCP_SYSTEM, (int) command.getControllerNumber());
        assertEquals(0, command.getNumber());
        assertEquals(-1, command.getId());
        assertEquals(-1, command.getGatewayId());
    }

    @Test
    public void test_queryActiveLoadsCommandString() {
        assertEquals("^G", interpreter.getQueryActiveLoadsCommandString(Interpreter.SINGLE_MCP_SYSTEM));
        assertEquals("^g1", interpreter.getQueryActiveLoadsCommandString(1));
    }

    @Test
    public void test_processActiveLoads_allByScene() {
        Integer[] array = new Integer[192];
        for (int i = 0; i < 192; i++) {
            array[i] = i + 1;
        }

        // all lights by ^C001
        testLoadsActive(interpreter.processActiveLoadsResponse(
                "FFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFF"
        ), array);

        array = new Integer[191];
        for (int i = 0; i < 191; i++) {
            array[i] = i + 2;
        }

        // all except load 1 (^C001^B001)
        testLoadsActive(interpreter.processActiveLoadsResponse(
                "FEFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFF"
        ), array);
    }

    @Test(expected=RuntimeException.class)
    public void test_processActiveLoads_nonHex() {
        testLoadsActive(interpreter.processActiveLoadsResponse(
                "0100000000000G0000000000000000000000000000000000"
        ), 1);
    }

    @Test(expected=RuntimeException.class)
    public void test_processActiveLoads_badLength() {
        testLoadsActive(interpreter.processActiveLoadsResponse(
                "01000000000000000000"
        ), 1);
    }

    @Test
    public void test_processActiveLoads() {
        testLoadsActive(interpreter.processActiveLoadsResponse(
                "010000000000000000000000000000000000000000000000"
        ), 1);

        testLoadsActive(interpreter.processActiveLoadsResponse(
                "050000000000000000000000000000000000000000000000"
        ), 1, 3);

        testLoadsActive(interpreter.processActiveLoadsResponse(
                "0D0000000000000000000000000000000000000000000000"
        ), 1, 3, 4);

        testLoadsActive(interpreter.processActiveLoadsResponse(
                "6D0000000000000000000000000000000000000000000000"
        ), 1, 3, 4, 6, 7);

        testLoadsActive(interpreter.processActiveLoadsResponse(
                "FF3000000000000000000000000000000000000000000000"
        ), 1, 2, 3, 4, 5, 6, 7, 8, 13, 14);

        testLoadsActive(interpreter.processActiveLoadsResponse(
                "FF7000000000000000000000000000000000000000000000"
        ), 1, 2, 3, 4, 5, 6, 7, 8, 13, 14, 15);
    }

    @Test
    public void test_convertToBinary() {
        assertEquals("0000", interpreter.convertToBinary("0"));
        assertEquals("0001", interpreter.convertToBinary("1"));
        assertEquals("0010", interpreter.convertToBinary("2"));
        assertEquals("0011", interpreter.convertToBinary("3"));
        assertEquals("0100", interpreter.convertToBinary("4"));
        assertEquals("0101", interpreter.convertToBinary("5"));
        assertEquals("0110", interpreter.convertToBinary("6"));
        assertEquals("0111", interpreter.convertToBinary("7"));
        assertEquals("1000", interpreter.convertToBinary("8"));
        assertEquals("1001", interpreter.convertToBinary("9"));
        assertEquals("1010", interpreter.convertToBinary("A"));
        assertEquals("1011", interpreter.convertToBinary("B"));
        assertEquals("1100", interpreter.convertToBinary("C"));
        assertEquals("1101", interpreter.convertToBinary("D"));
        assertEquals("1110", interpreter.convertToBinary("E"));
        assertEquals("1111", interpreter.convertToBinary("F"));
    }

    private void testLoadsActive(Map<Integer, Executor.LoadStatus> map, Integer... expected) {
        for (Integer i : expected) {
            assertEquals("load number: " + i, Executor.LoadStatus.ON, map.get(i));
            map.remove(i);
        }

        Iterator it = map.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry)it.next();
            assertEquals("load number: " + pair.getKey(), Executor.LoadStatus.OFF, pair.getValue());
            it.remove();
        }
    }
}