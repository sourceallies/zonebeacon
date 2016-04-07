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

import com.sourceallies.android.zonebeacon.api.executor.Executor;
import com.sourceallies.android.zonebeacon.data.StatefulButton;
import com.sourceallies.android.zonebeacon.data.StatefulZone;
import com.sourceallies.android.zonebeacon.data.model.Button;
import com.sourceallies.android.zonebeacon.data.model.Command;
import com.sourceallies.android.zonebeacon.data.model.CommandType;
import com.sourceallies.android.zonebeacon.data.model.Zone;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;

public class OnOffStatusUtilTest {

    OnOffStatusUtil util;

    @Before
    public void setUp() {
        util = new OnOffStatusUtil(getButtonList(0), getZoneList(0), getMap());
    }

    @Test
    public void test_getButtons() {
        List<StatefulButton> buttons = util.getStatefulButtons();

        assertEquals(Executor.LoadStatus.OFF, buttons.get(0).getLoadStatus());
        assertEquals(Executor.LoadStatus.OFF, buttons.get(1).getLoadStatus());
        assertEquals(Executor.LoadStatus.ON , buttons.get(2).getLoadStatus());
        assertEquals(Executor.LoadStatus.OFF, buttons.get(3).getLoadStatus());
        assertEquals(Executor.LoadStatus.OFF, buttons.get(4).getLoadStatus());
        assertEquals(Executor.LoadStatus.ON , buttons.get(5).getLoadStatus());
        assertEquals(Executor.LoadStatus.OFF, buttons.get(6).getLoadStatus());
    }

    @Test
    public void test_getZones() {
        List<StatefulZone> zones = util.getStatefulZones();

        assertEquals(Executor.LoadStatus.OFF, zones.get(0).getLoadStatus());
        assertEquals(Executor.LoadStatus.ON , zones.get(1).getLoadStatus());
        assertEquals(Executor.LoadStatus.OFF, zones.get(2).getLoadStatus());
        assertEquals(Executor.LoadStatus.ON , zones.get(3).getLoadStatus());
        assertEquals(Executor.LoadStatus.OFF, zones.get(4).getLoadStatus());
    }

    @Test
    public void test_invalidate() {
        assertNull(util.statefulButtons);
        assertNull(util.statefulZones);

        util.getStatefulButtons();
        util.getStatefulZones();

        assertNotNull(util.statefulButtons);
        assertNotNull(util.statefulZones);

        util.invalidate();

        assertNull(util.statefulButtons);
        assertNull(util.statefulZones);
    }

    @Test
    public void test_reGetButtonList() {
        List<StatefulButton> buttons = util.getStatefulButtons();
        assertEquals(buttons, util.getStatefulButtons());
    }

    @Test
    public void test_reGetZoneList() {
        List<StatefulZone> zones = util.getStatefulZones();
        assertEquals(zones, util.getStatefulZones());
    }

    @Test
    public void test_setState() {
        util.setState(0, 2, Executor.LoadStatus.ON);
        List<StatefulButton> buttons = util.getStatefulButtons();

        assertEquals(Executor.LoadStatus.OFF, buttons.get(0).getLoadStatus());
        assertEquals(Executor.LoadStatus.ON , buttons.get(1).getLoadStatus());
        assertEquals(Executor.LoadStatus.ON , buttons.get(2).getLoadStatus());
        assertEquals(Executor.LoadStatus.OFF, buttons.get(3).getLoadStatus());
        assertEquals(Executor.LoadStatus.OFF, buttons.get(4).getLoadStatus());
        assertEquals(Executor.LoadStatus.ON , buttons.get(5).getLoadStatus());
        assertEquals(Executor.LoadStatus.OFF, buttons.get(6).getLoadStatus());
    }

    @Test
    public void test_setStates() {
        List<Command> commands = new ArrayList();
        for (int i = 1; i < 7; i++) {
            Command c = new Command();
            c.setNumber(i);

            CommandType type = new CommandType();
            type.setActivateControllerSelection(false);
            c.setCommandType(type);

            commands.add(c);
        }

        util.setStates(commands, Executor.LoadStatus.OFF);
        List<StatefulButton> buttons = util.getStatefulButtons();

        assertEquals(Executor.LoadStatus.OFF, buttons.get(0).getLoadStatus());
        assertEquals(Executor.LoadStatus.OFF, buttons.get(1).getLoadStatus());
        assertEquals(Executor.LoadStatus.OFF, buttons.get(2).getLoadStatus());
        assertEquals(Executor.LoadStatus.OFF, buttons.get(3).getLoadStatus());
        assertEquals(Executor.LoadStatus.OFF, buttons.get(4).getLoadStatus());
        assertEquals(Executor.LoadStatus.OFF, buttons.get(5).getLoadStatus());
        assertEquals(Executor.LoadStatus.OFF, buttons.get(6).getLoadStatus());
    }

    @Test
    public void test_setStates_multiMcp() {
        util = new OnOffStatusUtil(getButtonList(1), getZoneList(1), getMap());

        List<Command> commands = new ArrayList();
        for (int i = 1; i < 7; i++) {
            Command c = new Command();
            c.setNumber(i);
            c.setControllerNumber(1);

            CommandType type = new CommandType();
            type.setActivateControllerSelection(true);
            c.setCommandType(type);

            commands.add(c);
        }

        util.setStates(commands, Executor.LoadStatus.OFF);
        List<StatefulButton> buttons = util.getStatefulButtons();

        assertEquals(Executor.LoadStatus.OFF, buttons.get(0).getLoadStatus());
        assertEquals(Executor.LoadStatus.OFF, buttons.get(1).getLoadStatus());
        assertEquals(Executor.LoadStatus.OFF, buttons.get(2).getLoadStatus());
        assertEquals(Executor.LoadStatus.OFF, buttons.get(3).getLoadStatus());
        assertEquals(Executor.LoadStatus.OFF, buttons.get(4).getLoadStatus());
        assertEquals(Executor.LoadStatus.OFF, buttons.get(5).getLoadStatus());
        assertEquals(Executor.LoadStatus.OFF, buttons.get(6).getLoadStatus());
    }

    @Test
    public void test_getStatus() {
        Map<Integer, Map<Integer, Executor.LoadStatus>> map = new HashMap();
        Map<Integer, Executor.LoadStatus> controller0 = new HashMap();
        controller0.put(1, Executor.LoadStatus.OFF);
        controller0.put(2, Executor.LoadStatus.ON);

        map.put(0, controller0);
        map.put(1, controller0);

        Command command = new Command();
        CommandType type = new CommandType();

        type.setActivateControllerSelection(false);
        command.setNumber(1);
        command.setControllerNumber(1);
        command.setCommandType(type);

        assertEquals(Executor.LoadStatus.OFF, util.getStatus(map, command));

        type.setActivateControllerSelection(false);
        command.setNumber(2);
        command.setControllerNumber(1);
        command.setCommandType(type);

        assertEquals(Executor.LoadStatus.ON, util.getStatus(map, command));

        type.setActivateControllerSelection(true);
        command.setNumber(1);
        command.setControllerNumber(1);
        command.setCommandType(type);

        assertEquals(Executor.LoadStatus.OFF, util.getStatus(map, command));

        type.setActivateControllerSelection(true);
        command.setNumber(2);
        command.setControllerNumber(1);
        command.setCommandType(type);

        assertEquals(Executor.LoadStatus.ON, util.getStatus(map, command));

        type.setActivateControllerSelection(true);
        command.setNumber(3);
        command.setControllerNumber(1);
        command.setCommandType(type);

        assertEquals(Executor.LoadStatus.OFF, util.getStatus(map, command));
    }

    private List<Zone> getZoneList(int controllerNumber) {
        List<Zone> zones = new ArrayList<>();

        zones.add(getZone(controllerNumber, 0, 1));               // OFF
        zones.add(getZone(controllerNumber, 2));                  // ON
        zones.add(getZone(controllerNumber, 1, 2, 3));            // OFF
        zones.add(getZone(controllerNumber, 2, 5));               // ON
        zones.add(getZone(controllerNumber, 3, 4, 5, 6));         // OFF

        return zones;
    }

    private List<Button> getButtonList(int controllerNumber) {
        List<Button> buttons = new ArrayList<>();

        buttons.add(getButton(controllerNumber, 1, 2));           // 0: OFF
        buttons.add(getButton(controllerNumber, 2, 3));           // 1: OFF
        buttons.add(getButton(controllerNumber, 3, 4));           // 2: ON
        buttons.add(getButton(controllerNumber, 1, 3));           // 3: OFF
        buttons.add(getButton(controllerNumber, 1));              // 4: OFF
        buttons.add(getButton(controllerNumber, 3));              // 5: ON
        buttons.add(getButton(controllerNumber, 1, 2, 3, 4));     // 6: OFF

        return buttons;
    }

    private List<Button> getButtonSubList(int controllerNumber, int... indexes) {
        List<Button> sublist = new ArrayList();
        List<Button> buttons = getButtonList(controllerNumber);

        for (int index : indexes) {
            sublist.add(buttons.get(index));
        }

        return sublist;
    }

    private Zone getZone(int controllerNumber, int... buttonIndexes) {
        Zone zone = new Zone();
        zone.setButtons(getButtonSubList(controllerNumber, buttonIndexes));
        return zone;
    }

    private Button getButton(int controllerNumber, int... commandNumbers) {
        List<Command> commands = new ArrayList();
        for (int num : commandNumbers)
            commands.add(getCommand(controllerNumber, num));

        Button button = new Button();
        button.setCommands(commands);
        return button;
    }

    private Command getCommand(int controllerNumber, int number) {
        Command command = new Command();
        command.setNumber(number);
        command.setControllerNumber(controllerNumber);

        CommandType type = new CommandType();
        type.setActivateControllerSelection(controllerNumber != 0);
        command.setCommandType(type);

        return command;
    }

    private Map<Integer, Map<Integer, Executor.LoadStatus>> getMap() {
        Map<Integer, Executor.LoadStatus> map = new HashMap();

        map.put(1, Executor.LoadStatus.OFF);
        map.put(2, Executor.LoadStatus.OFF);
        map.put(3, Executor.LoadStatus.ON);
        map.put(4, Executor.LoadStatus.ON);

        Map<Integer, Map<Integer, Executor.LoadStatus>> fullMap = new HashMap();
        fullMap.put(0, map);

        return fullMap;
    }
}