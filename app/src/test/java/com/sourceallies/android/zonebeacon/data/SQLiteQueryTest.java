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

package com.sourceallies.android.zonebeacon.data;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.sourceallies.android.zonebeacon.ZoneBeaconRobolectricSuite;
import com.sourceallies.android.zonebeacon.data.model.Button;
import com.sourceallies.android.zonebeacon.data.model.CommandType;
import com.sourceallies.android.zonebeacon.data.model.Gateway;
import com.sourceallies.android.zonebeacon.data.model.Command;
import com.sourceallies.android.zonebeacon.data.model.SystemType;
import com.sourceallies.android.zonebeacon.data.model.Zone;
import com.sourceallies.android.zonebeacon.util.FixtureLoader;

import org.junit.Before;
import org.junit.Test;
import org.robolectric.RuntimeEnvironment;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class SQLiteQueryTest extends ZoneBeaconRobolectricSuite {

    private DataSource source;

    @Before
    public void setUp() throws Exception {
        SQLiteDatabase database = SQLiteDatabase.create(null);
        DatabaseSQLiteHelper helper = new DatabaseSQLiteHelper(RuntimeEnvironment.application);
        helper.onCreate(database);

        source = new DataSource(database);
        insertData();
    }

    @Test
    public void test_databaseCreated() {
        assertNotNull(source.getDatabase());

        int numTables = 0;
        Cursor cursor = source.getDatabase().rawQuery("SELECT count(*) FROM sqlite_master " +
                "WHERE type = 'table' AND name != 'android_metadata' AND name != " +
                "'sqlite_sequence';", null);
        if (cursor != null && cursor.moveToFirst()) {
            numTables = cursor.getInt(0);
            cursor.close();
        }

        assertTrue(numTables > 0);
    }

    @Test
    public void test_findGateway() {
        Gateway gateway = source.findGateway(1L);
        assertEquals("Gateway 1", gateway.getName());
    }

    @Test
    public void test_insertNewGateway() {
        int originalNum = getTableCount("gateway");
        source.insertNewGateway("Test Gateway", "192.168.1.100", 11000);
        int newGateways = getTableCount("gateway") - originalNum;

        assertEquals(1, newGateways);
    }

    @Test
    public void test_deleteGateway() {
        source.insertNewGateway("Test Gateway", "192.168.1.100", 11000);

        int originalNum = getTableCount("gateway");
        source.deleteGateway(1);
        int newGateways = getTableCount("gateway") - originalNum;

        assertEquals(-1, newGateways);
    }

    @Test
    public void test_findGateways() {
        List<Gateway> gateways = source.findGateways();

        assertEquals(2, gateways.size());
        assertEquals("Gateway 1", gateways.get(0).getName());
        assertEquals("Gateway 2", gateways.get(1).getName());
    }

    @Test
    public void test_findCommandTypes() {
        Gateway gateway = new Gateway();
        gateway.setSystemTypeId(1L);

        List<CommandType> commandTypes = source.findCommandTypes(gateway);
        assertEquals(8, commandTypes.size());
        assertEquals("Single MCP - Load/Relay", commandTypes.get(0).getName());
        assertEquals("Single MCP - Switch", commandTypes.get(1).getName());
        assertEquals("Single MCP - Scene", commandTypes.get(2).getName());
        assertEquals("Multi MCP - Load/Relay", commandTypes.get(3).getName());
        assertEquals("Multi MCP - Switch", commandTypes.get(4).getName());
        assertEquals("Multi MCP - Scene", commandTypes.get(5).getName());
        assertEquals("Single MCP - Brightness", commandTypes.get(6).getName());
        assertEquals("Multi MCP - Brightness", commandTypes.get(7).getName());
    }

    @Test
    public void test_findCommandTypes_shownInList() {
        Gateway gateway = new Gateway();
        gateway.setSystemTypeId(1L);

        List<CommandType> commandTypes = source.findCommandTypesShownInUI(gateway);
        assertEquals(6, commandTypes.size());
        assertEquals("Single MCP - Load/Relay", commandTypes.get(0).getName());
        assertEquals("Single MCP - Switch", commandTypes.get(1).getName());
        assertEquals("Single MCP - Scene", commandTypes.get(2).getName());
        assertEquals("Multi MCP - Load/Relay", commandTypes.get(3).getName());
        assertEquals("Multi MCP - Switch", commandTypes.get(4).getName());
        assertEquals("Multi MCP - Scene", commandTypes.get(5).getName());
    }

    @Test
    public void test_findCommandTypes_notShownInList() {
        Gateway gateway = new Gateway();
        gateway.setSystemTypeId(1L);

        List<CommandType> commandTypes = source.findCommandTypesNotShownInUI(gateway);
        assertEquals(2, commandTypes.size());
        assertEquals("Single MCP - Brightness", commandTypes.get(0).getName());
        assertEquals("Multi MCP - Brightness", commandTypes.get(1).getName());
    }

    @Test
    public void test_findSystemTypes() {
        List<SystemType> systemTypes = source.findSystemTypes();
        assertEquals(1, systemTypes.size());
        assertEquals("CentraLite Elegance", systemTypes.get(0).getName());
    }

    @Test
    public void test_insertNewCommand() {
        int originalNum = getTableCount("command");
        source.insertNewCommand("command", 1, 1, 1, null);
        int newCommands = getTableCount("command") - originalNum;

        assertEquals(1, newCommands);
    }

    @Test
    public void test_deleteCommand() {
        source.insertNewCommand("command", 1, 1, 1, null);

        int originalNum = getTableCount("command");
        source.deleteCommand(1);
        int newCommands = getTableCount("command") - originalNum;

        assertEquals(-1, newCommands);
    }

    @Test
    public void test_findCommands() {
        assertEquals(8, source.findCommands(1).size());
        assertEquals(8, source.findCommands(2).size());

        source.insertNewCommand("command", 1, 1, 1, null);

        assertEquals(9, source.findCommands(1).size());
        assertEquals(8, source.findCommands(2).size());

        source.insertNewCommand("command", 2, 1, 1, null);
        long id = source.insertNewCommand("command", 2, 1, 1, null);

        assertEquals(9, source.findCommands(1).size());
        assertEquals(10, source.findCommands(2).size());

        source.deleteCommand(id);

        assertEquals(9, source.findCommands(1).size());
        assertEquals(9, source.findCommands(2).size());
    }

    @Test
    public void test_insertNewButton() {
        int originalNum = getTableCount("button");
        List<Command> commands = source.findCommands(1);
        source.insertNewButton("button", commands);
        int newButtons = getTableCount("button") - originalNum;

        assertEquals(1, newButtons);
    }

    @Test
    public void test_deleteButton() {
        List<Command> commands = source.findCommands(1);
        source.insertNewButton("button", commands);

        int originalNum = getTableCount("button");
        int originalButtonCommandLinkCount = getTableCount("button_command_link");
        source.deleteButton(1);
        int newButtons = getTableCount("button") - originalNum;
        int newButtonCommandLinks = getTableCount("button_command_link") - originalButtonCommandLinkCount;

        assertEquals(-1, newButtons);
        assertEquals(-2, newButtonCommandLinks);
    }

    @Test
    public void test_insertNewZone() {
        int originalNum = getTableCount("zone");

        List<Button> buttons = new ArrayList<>();
        Button button = new Button();
        button.setId(1);
        buttons.add(button);

        source.insertNewZone("zone", buttons);
        int newZones = getTableCount("zone") - originalNum;

        assertEquals(1, newZones);
    }

    @Test
    public void test_deleteZone() {
        List<Button> buttons = new ArrayList<>();
        Button button = new Button();
        button.setId(1);
        buttons.add(button);
        source.insertNewZone("zone", buttons);

        int originalNum = getTableCount("zone");
        int originalZoneButtonLinkNum = getTableCount("zone_button_link");
        source.deleteZone(1);
        int newZones = getTableCount("zone") - originalNum;
        int newZoneButtonLinkNum = getTableCount("zone_button_link") - originalZoneButtonLinkNum;

        assertEquals(-1, newZones);
        assertEquals(-3, newZoneButtonLinkNum);
    }

    @Test
    public void test_findButtons() {
        Gateway gateway = new Gateway();
        gateway.setId(1);

        List<Button> buttons = source.findButtons(gateway);

        assertEquals(5, buttons.size());
        assertEquals(2, buttons.get(0).getCommands().size());
        assertEquals(1, buttons.get(1).getCommands().size());
        assertEquals(3, buttons.get(2).getCommands().size());
        assertEquals(1, buttons.get(3).getCommands().size());
        assertEquals(2, buttons.get(4).getCommands().size());
        assertEquals(1, buttons.get(0).getId());
        assertEquals(2, buttons.get(1).getId());
        assertEquals(3, buttons.get(2).getId());
        assertEquals(4, buttons.get(3).getId());
        assertEquals(5, buttons.get(4).getId());

        gateway.setId(2);

        buttons = source.findButtons(gateway);

        assertEquals(5, buttons.size());
        assertEquals(3, buttons.get(0).getCommands().size());
        assertEquals(1, buttons.get(1).getCommands().size());
        assertEquals(2, buttons.get(2).getCommands().size());
        assertEquals(1, buttons.get(3).getCommands().size());
        assertEquals(2, buttons.get(4).getCommands().size());
        assertEquals(6, buttons.get(0).getId());
        assertEquals(7, buttons.get(1).getId());
        assertEquals(8, buttons.get(2).getId());
        assertEquals(9, buttons.get(3).getId());
        assertEquals(10, buttons.get(4).getId());

    }

    @Test
    public void test_findZones() {
        Gateway gateway = new Gateway();
        gateway.setId(1);

        List<Zone> zones = source.findZones(gateway);

        assertEquals(2, zones.size());
        assertEquals(3, zones.get(0).getButtons().size());
        assertEquals(2, zones.get(1).getButtons().size());
        assertEquals(1, zones.get(0).getId());
        assertEquals(2, zones.get(1).getId());
        assertEquals(2, zones.get(0).getButtons().get(0).getCommands().size());
        assertEquals(1, zones.get(0).getButtons().get(1).getCommands().size());
        assertEquals(3, zones.get(0).getButtons().get(2).getCommands().size());
        assertEquals(1, zones.get(0).getButtons().get(0).getId());
        assertEquals(2, zones.get(0).getButtons().get(1).getId());
        assertEquals(3, zones.get(0).getButtons().get(2).getId());
        assertEquals(3, zones.get(1).getButtons().get(0).getCommands().size());
        assertEquals(1, zones.get(1).getButtons().get(1).getCommands().size());
        assertEquals(3, zones.get(1).getButtons().get(0).getId());
        assertEquals(4, zones.get(1).getButtons().get(1).getId());

        gateway.setId(2);

        zones = source.findZones(gateway);

        assertEquals(3, zones.size());
        assertEquals(4, zones.get(0).getButtons().size());
        assertEquals(2, zones.get(1).getButtons().size());
        assertEquals(3, zones.get(2).getButtons().size());
        assertEquals(3, zones.get(0).getId());
        assertEquals(4, zones.get(1).getId());
        assertEquals(5, zones.get(2).getId());
        assertEquals(3, zones.get(0).getButtons().get(0).getCommands().size());
        assertEquals(1, zones.get(0).getButtons().get(1).getCommands().size());
        assertEquals(2, zones.get(0).getButtons().get(2).getCommands().size());
        assertEquals(1, zones.get(0).getButtons().get(3).getCommands().size());
        assertEquals(6, zones.get(0).getButtons().get(0).getId());
        assertEquals(7, zones.get(0).getButtons().get(1).getId());
        assertEquals(8, zones.get(0).getButtons().get(2).getId());
        assertEquals(9, zones.get(0).getButtons().get(3).getId());
        assertEquals(1, zones.get(1).getButtons().get(0).getCommands().size());
        assertEquals(2, zones.get(1).getButtons().get(1).getCommands().size());
        assertEquals(9, zones.get(1).getButtons().get(0).getId());
        assertEquals(10, zones.get(1).getButtons().get(1).getId());
        assertEquals(3, zones.get(2).getButtons().get(0).getCommands().size());
        assertEquals(1, zones.get(2).getButtons().get(1).getCommands().size());
        assertEquals(2, zones.get(2).getButtons().get(2).getCommands().size());
        assertEquals(6, zones.get(2).getButtons().get(0).getId());
        assertEquals(7, zones.get(2).getButtons().get(1).getId());
        assertEquals(10, zones.get(2).getButtons().get(2).getId());
    }

    private int getTableCount(String table) {
        Cursor cursor = source.getDatabase().rawQuery("SELECT count(*) FROM " + table, null);
        if (cursor != null && cursor.moveToFirst()) {
            int num = cursor.getInt(0);
            cursor.close();

            return num;
        }

        throw new RuntimeException("Error finding table count");
    }

    private void insertData() throws Exception {
        SQLiteDatabase database = source.getDatabase();
        FixtureLoader loader = new FixtureLoader();
        loader.loadFixturesToDatabase(database);
    }

}