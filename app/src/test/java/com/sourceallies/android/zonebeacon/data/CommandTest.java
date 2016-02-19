/*
 * Copyright (C) 2016 Source Allies
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

package com.sourceallies.android.zonebeacon.data;

import com.sourceallies.android.zonebeacon.ZoneBeaconSuite;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class CommandTest extends ZoneBeaconSuite {

    private Command command;

    @Before
    public void setUp() {
        command = new Command("testip", 80);
    }

    @Test
    public void test_notNull() {
        assertNull(command.getCallback());
        assertNotNull(command.getHost());
        assertNotNull(command.getPort());
        assertNotNull(command.getHandler());
    }

    @Test
    public void test_setCommand() {
        assertEquals(command, command.setCommand("test"));
        assertNotNull(command.getCommand());
    }

    @Test
    public void test_setResponseCallback() {
        assertEquals(command, command.setResponseCallback(new Command.CommandCallback() {
            @Override
            public void onResponse(String text) {

            }
        }));

        assertNotNull(command.getCallback());
    }

}