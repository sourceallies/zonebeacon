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
import com.sourceallies.android.zonebeacon.data.OnOffButton;
import com.sourceallies.android.zonebeacon.data.OnOffZone;
import com.sourceallies.android.zonebeacon.data.model.Button;
import com.sourceallies.android.zonebeacon.data.model.Command;
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
        util = new OnOffStatusUtil(getButtonList(), getZoneList(), getMap());
    }

    @Test
    public void test_getButtons() {
        List<OnOffButton> buttons = util.getOnOffButtons();

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
        List<OnOffZone> zones = util.getOnOffZones();

        assertEquals(Executor.LoadStatus.OFF, zones.get(0).getLoadStatus());
        assertEquals(Executor.LoadStatus.ON , zones.get(1).getLoadStatus());
        assertEquals(Executor.LoadStatus.OFF, zones.get(2).getLoadStatus());
        assertEquals(Executor.LoadStatus.ON , zones.get(3).getLoadStatus());
        assertEquals(Executor.LoadStatus.OFF, zones.get(4).getLoadStatus());
    }

    @Test
    public void test_invalidate() {
        assertNull(util.onOffButtons);
        assertNull(util.onOffZones);

        util.getOnOffButtons();
        util.getOnOffZones();

        assertNotNull(util.onOffButtons);
        assertNotNull(util.onOffZones);

        util.invalidate();

        assertNull(util.onOffButtons);
        assertNull(util.onOffZones);
    }

    @Test
    public void test_reGetButtonList() {
        List<OnOffButton> buttons = util.getOnOffButtons();
        assertEquals(buttons, util.getOnOffButtons());
    }

    @Test
    public void test_reGetZoneList() {
        List<OnOffZone> zones = util.getOnOffZones();
        assertEquals(zones, util.getOnOffZones());
    }

    private List<Zone> getZoneList() {
        List<Zone> zones = new ArrayList<>();

        zones.add(getZone(0, 1));               // OFF
        zones.add(getZone(2));                  // ON
        zones.add(getZone(1, 2, 3));            // OFF
        zones.add(getZone(2, 5));               // ON
        zones.add(getZone(3, 4, 5, 6));         // OFF

        return zones;
    }

    private List<Button> getButtonList() {
        List<Button> buttons = new ArrayList<>();

        buttons.add(getButton(1, 2));           // 0: OFF
        buttons.add(getButton(2, 3));           // 1: OFF
        buttons.add(getButton(3, 4));           // 2: ON
        buttons.add(getButton(1, 3));           // 3: OFF
        buttons.add(getButton(1));              // 4: OFF
        buttons.add(getButton(3));              // 5: ON
        buttons.add(getButton(1, 2, 3, 4));     // 6: OFF

        return buttons;
    }

    private List<Button> getButtonSubList(int... indexes) {
        List<Button> sublist = new ArrayList();
        List<Button> buttons = getButtonList();

        for (int index : indexes) {
            sublist.add(buttons.get(index));
        }

        return sublist;
    }

    private Zone getZone(int... buttonIndexes) {
        Zone zone = new Zone();
        zone.setButtons(getButtonSubList(buttonIndexes));
        return zone;
    }

    private Button getButton(int... commandNumbers) {
        List<Command> commands = new ArrayList();
        for (int num : commandNumbers)
            commands.add(getCommand(num));

        Button button = new Button();
        button.setCommands(commands);
        return button;
    }

    private Command getCommand(int number) {
        Command command = new Command();
        command.setNumber(number);
        return command;
    }

    private Map<Integer, Executor.LoadStatus> getMap() {
        Map<Integer, Executor.LoadStatus> map = new HashMap();

        map.put(1, Executor.LoadStatus.OFF);
        map.put(2, Executor.LoadStatus.OFF);
        map.put(3, Executor.LoadStatus.ON);
        map.put(4, Executor.LoadStatus.ON);

        return new HashMap();
    }
}