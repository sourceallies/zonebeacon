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

package com.sourceallies.android.zonebeacon.util;

import com.google.inject.Inject;
import com.sourceallies.android.zonebeacon.data.model.Button;
import com.sourceallies.android.zonebeacon.data.model.Command;
import com.sourceallies.android.zonebeacon.data.model.CommandType;
import com.sourceallies.android.zonebeacon.data.model.Zone;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class ControllerUtilTest {

    private ControllerUtil util;

    @Before
    public void setUp() {
        util = new ControllerUtil();
    }

    @Test
    public void test_buttons_empty() {
        List<Button> buttons = new ArrayList();
        List<Integer> controllers = util.getControllerNumbersByButtons(buttons);

        assertEquals(1, controllers.size());
        assertEquals(Integer.valueOf(-1), controllers.get(0));
    }

    @Test
    public void test_buttons_singleMCP() {
        List<Button> buttons = getButtons(false);
        List<Integer> controllers = util.getControllerNumbersByButtons(buttons);

        assertEquals(1, controllers.size());
        assertEquals(Integer.valueOf(-1), controllers.get(0));
    }

    @Test
    public void test_buttons_multiMCP() {
        List<Button> buttons = getButtons(true);
        List<Integer> controllers = util.getControllerNumbersByButtons(buttons);

        assertEquals(5, controllers.size());
        for (int i = 0; i < 5; i++) {
            assertEquals(Integer.valueOf(i), controllers.get(i));
        }
    }

    @Test
    public void test_zones_empty() {
        List<Zone> zones = new ArrayList();
        List<Integer> controllers = util.getControllerNumbersByZones(zones);

        assertEquals(1, controllers.size());
        assertEquals(Integer.valueOf(-1), controllers.get(0));
    }

    @Test
    public void test_zones_singleMCP() {
        List<Zone> zones = getZones(false);
        List<Integer> controllers = util.getControllerNumbersByZones(zones);

        assertEquals(1, controllers.size());
        assertEquals(Integer.valueOf(-1), controllers.get(0));
    }

    @Test
    public void test_zones_multiMCP() {
        List<Zone> zones = getZones(true);
        List<Integer> controllers = util.getControllerNumbersByZones(zones);

        assertEquals(5, controllers.size());
        for (int i = 0; i < 5; i++) {
            assertEquals(Integer.valueOf(i), controllers.get(i));
        }
    }

    private List<Zone> getZones(boolean multiMcp) {
        List<Zone> zones = new ArrayList();

        Zone zone = new Zone();
        zone.setButtons(getButtons(multiMcp));
        zones.add(zone);

        return zones;
    }

    private List<Button> getButtons(boolean multiMcp) {
        List<Button> buttons = new ArrayList();

        List<Command> commandsOne = new ArrayList();
        Button one = new Button();
        one.setName("button one");
        one.setCommands(commandsOne);
        buttons.add(one);

        for (int i = 0; i < 5; i++) {
            CommandType type = new CommandType();
            type.setActivateControllerSelection(multiMcp);

            Command command = new Command();
            command.setControllerNumber(i);
            command.setNumber(i);
            command.setCommandType(type);
            commandsOne.add(command);
        }

        return buttons;
    }
}
