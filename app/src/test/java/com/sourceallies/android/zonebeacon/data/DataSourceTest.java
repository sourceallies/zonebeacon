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

import android.content.ContentValues;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.database.sqlite.SQLiteDatabase;

import com.sourceallies.android.zonebeacon.ZoneBeaconRobolectricSuite;
import com.sourceallies.android.zonebeacon.data.model.*;
import com.sourceallies.android.zonebeacon.data.model.Command;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.robolectric.RuntimeEnvironment;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class DataSourceTest extends ZoneBeaconRobolectricSuite {

    private DataSource source;

    @Mock
    private SQLiteDatabase database;
    @Mock
    private DatabaseSQLiteHelper helper;
    @Mock
    private Cursor cursor;

    @Before
    public void setUp() {
        source = new DataSource(helper);
        when(helper.getWritableDatabase()).thenReturn(database);
        source.open();
    }

    @After
    public void tearDown() {
        source.close();
        verify(helper).close();
    }

    @Test
    public void test_realConstructor() {
        DataSource dataSource = DataSource.getInstance(RuntimeEnvironment.application);
        dataSource.open();
        dataSource.close();
    }

    @Test
    public void test_getDatabase() {
        assertEquals(database, source.getDatabase());
    }

    @Test
    public void test_beginTransaction() {
        source.beginTransaction();
        verify(database).beginTransaction();
    }

    @Test
    public void test_setTransactionSuccessful() {
        source.setTransactionSuccessful();
        verify(database).setTransactionSuccessful();
    }

    @Test
    public void test_endTransaction() {
        source.endTransaction();
        verify(database).endTransaction();
    }

    @Test
    public void test_execSql() {
        source.execSql("test sql");
        verify(database).execSQL("test sql");
    }

    @Test
    public void test_rawQuery() {
        source.rawQuery("test sql");
        verify(database).rawQuery("test sql", null);
    }

    @Test
    public void test_insertGateway() {
        source.insertNewGateway("Test Gateway", "192.168.1.100", 11000);
        verify(database).insert(Mockito.eq(Gateway.TABLE_GATEWAY), anyString(), any(ContentValues.class));
    }

    @Test
    public void test_deleteGateway() {
        source.deleteGateway(1);
        verify(database).delete(Gateway.TABLE_GATEWAY, "_id = 1", null);
    }

    @Test
    public void test_getGateway() {
        MatrixCursor cursor = new MatrixCursor(new String[] {
                "_id", "name", "ip_address", "port_number", "system_type_id"
        });

        cursor.addRow(new String[] {"1", "gateway 1", "192.168.1.100", "11000", "1"});

        when(database.rawQuery(anyString(), any(String[].class))).thenReturn(cursor);
        Gateway gateway = source.findGateway(1L);

        assertEquals("gateway 1", gateway.getName());
    }

    @Test
    public void test_getGateway_noRows() {
        MatrixCursor cursor = new MatrixCursor(new String[] {
                "_id", "name", "ip_address", "port_number", "system_type_id"
        });

        when(database.rawQuery(anyString(), any(String[].class))).thenReturn(cursor);
        Gateway gateway = source.findGateway(1L);

        assertNull(gateway.getName());
    }

    @Test
    public void test_getGateway_nullCursor() {
        when(database.rawQuery(anyString(), any(String[].class))).thenReturn(null);
        Gateway gateway = source.findGateway(1L);

        assertNull(gateway);
    }

    @Test
    public void test_findGateways() {
        MatrixCursor cursor = new MatrixCursor(new String[] {
                "_id", "name", "ip_address", "port_number", "system_type_id"
        });

        cursor.addRow(new String[] {"1", "gateway 1", "192.168.1.100", "11000", "1"});
        cursor.addRow(new String[] {"2", "gateway 2", "192.168.1.150", "11000", "1"});
        cursor.addRow(new String[] {"3", "gateway 3", "192.168.1.200", "11000", "1"});

        when(database.rawQuery(anyString(), any(String[].class))).thenReturn(cursor);
        List<Gateway> gateways = source.findGateways();

        assertEquals(3, gateways.size());
        assertEquals("gateway 1", gateways.get(0).getName());
        assertEquals("gateway 2", gateways.get(1).getName());
        assertEquals("gateway 3", gateways.get(2).getName());
    }

    @Test
    public void test_findGateways_noRows() {
        MatrixCursor cursor = new MatrixCursor(new String[] {
                "_id", "name", "ip_address", "port_number", "system_type_id"
        });

        when(database.rawQuery(anyString(), any(String[].class))).thenReturn(cursor);
        List<Gateway> gateways = source.findGateways();

        assertEquals(0, gateways.size());
    }

    @Test
    public void test_findGateways_nullCursor() {
        when(database.rawQuery(anyString(), any(String[].class))).thenReturn(null);
        List<Gateway> gateways = source.findGateways();

        assertEquals(0, gateways.size());
    }

    @Test
    public void test_insertCommand() {
        CommandType commandType = new CommandType();
        commandType.setId(1);

        source.insertNewCommand("command 1", 1, 1, commandType, 1);
        verify(database).insert(eq(Command.TABLE_COMMAND), anyString(), any(ContentValues.class));
    }

    @Test
    public void test_deleteCommand() {
        source.deleteCommand(1);
        verify(database).delete(Command.TABLE_COMMAND, "_id = 1", null);
    }

    @Test
    public void test_findCommands() {
        MatrixCursor cursor = new MatrixCursor(new String[] {
                "_id", "name", "gateway_id", "number", "command_type_id", "controller_number"
        });

        cursor.addRow(new String[] {"1", "command 1", "1", "1", "1", "1"});
        cursor.addRow(new String[] {"2", "command 2", "1", "1", "1", "1"});
        cursor.addRow(new String[] {"3", "command 3", "1", "1", "1", "1"});

        when(database.rawQuery(anyString(), any(String[].class))).thenReturn(cursor);

        Gateway gateway = new Gateway();
        gateway.setId(1);
        List<Command> commands = source.findCommands(gateway);

        assertEquals(3, commands.size());
        assertEquals("command 1", commands.get(0).getName());
        assertEquals("command 2", commands.get(1).getName());
        assertEquals("command 3", commands.get(2).getName());
    }

    @Test
    public void test_findCommands_noRows() {
        MatrixCursor cursor = new MatrixCursor(new String[] {
                "_id", "name", "gateway_id", "number", "command_type_id", "controller_number"
        });

        when(database.rawQuery(anyString(), any(String[].class))).thenReturn(cursor);
        List<Command> commands = source.findCommands(1);

        assertEquals(0, commands.size());
    }

    @Test
    public void test_findCommands_nullCursor() {
        when(database.rawQuery(anyString(), any(String[].class))).thenReturn(null);
        List<Command> commands = source.findCommands(1);

        assertEquals(0, commands.size());
    }

    @Test
    public void test_insertButton() {
        ContentValues values = new ContentValues(1);
        values.put("name", "test button");
        when(database.insert(Button.TABLE_BUTTON, null, values)).thenReturn(1L);

        List<Command> commands = new ArrayList<>();
        Command command1 = new Command();
        command1.setId(1);
        Command command2 = new Command();
        command2.setId(2);
        commands.add(command1);
        commands.add(command2);

        source.insertNewButton("test button", commands);

        verify(database).insert(Button.TABLE_BUTTON, null, values);
        verify(database, times(2)).insert(eq(ButtonCommandLink.TABLE_BUTTON_COMMAND_LINK),
                anyString(), any(ContentValues.class));
    }

    @Test(expected = RuntimeException.class)
    public void test_insertButton_nullCommands() {
        source.insertNewButton("test button", null);
    }

    @Test(expected = RuntimeException.class)
    public void test_insertButton_blankCommands() {
        source.insertNewButton("test button", new ArrayList<Command>());
    }

    @Test
    public void test_deleteButton() {
        when(database.delete("button", "_id = 1", null)).thenReturn(2);
        when(database.delete("button_command_link", "button_id = 1", null)).thenReturn(3);

        assertEquals(5, source.deleteButton(1));
    }

    @Test
    public void test_findButtons() {
        MatrixCursor cursor = new MatrixCursor(new String[] {
                "button_id", "button_name", "command_id", "command_name", "gateway_id", "number",
                "command_type_id", "controller_number", "command_type_name", "base_serial_on_code",
                "base_serial_off_code", "activate_controller_selection"
        });

        cursor.addRow(new String[] {"1", "button 1", "1", "command 1", "1", "1", "1", "1", "type 1", "^A", "^B", "0"});
        cursor.addRow(new String[] {"2", "button 2", "1", "command 1", "1", "1", "1", "1", "type 1", "^A", "^B", "0"});
        cursor.addRow(new String[] {"2", "button 2", "2", "command 2", "1", "1", "1", "1", "type 1", "^A", "^B", "0"});
        cursor.addRow(new String[] {"2", "button 2", "3", "command 3", "1", "1", "1", "1", "type 1", "^A", "^B", "0"});
        cursor.addRow(new String[] {"3", "button 3", "3", "command 3", "1", "1", "1", "1", "type 1", "^A", "^B", "0"});
        cursor.addRow(new String[] {"3", "button 3", "4", "command 4", "1", "1", "1", "1", "type 1", "^A", "^B", "0"});

        when(database.rawQuery(anyString(), any(String[].class))).thenReturn(cursor);

        Gateway gateway = new Gateway();
        gateway.setId(1);
        List<Button> buttons = source.findButtons(gateway);

        assertEquals(3, buttons.size());
        assertEquals("button 1", buttons.get(0).getName());
        assertEquals("button 2", buttons.get(1).getName());
        assertEquals("button 3", buttons.get(2).getName());

        assertEquals(1, buttons.get(0).getCommands().size());
        assertEquals("command 1", buttons.get(0).getCommands().get(0).getName());

        assertEquals(3, buttons.get(1).getCommands().size());
        assertEquals("command 1", buttons.get(1).getCommands().get(0).getName());
        assertEquals("command 2", buttons.get(1).getCommands().get(1).getName());
        assertEquals("command 3", buttons.get(1).getCommands().get(2).getName());

        assertEquals(2, buttons.get(2).getCommands().size());
        assertEquals("command 3", buttons.get(2).getCommands().get(0).getName());
        assertEquals("command 4", buttons.get(2).getCommands().get(1).getName());

        assertTrue(cursor.isClosed());
    }

    @Test
    public void test_findButtons_noRows() {
        MatrixCursor cursor = new MatrixCursor(new String[] {
                "button_id", "button_name", "command_id", "command_name", "gateway_id", "number",
                "command_type_id", "controller_number", "command_type_name", "base_serial_on_code",
                "base_serial_off_code", "activate_controller_selection"
        });

        when(database.rawQuery(anyString(), any(String[].class))).thenReturn(cursor);
        List<Button> buttons = source.findButtons(1);

        assertEquals(0, buttons.size());
        assertTrue(cursor.isClosed());
    }

    @Test
    public void test_findButtons_nullCursor() {
        when(database.rawQuery(anyString(), any(String[].class))).thenReturn(null);
        List<Button> buttons = source.findButtons(1);

        assertEquals(0, buttons.size());
    }

    @Test
    public void test_insertZone() {
        ContentValues values = new ContentValues(1);
        values.put("name", "test zone");
        when(database.insert(Zone.TABLE_ZONE, null, values)).thenReturn(1L);

        List<Button> buttons = new ArrayList<>();
        Button button1 = new Button();
        button1.setId(1);
        Button button2 = new Button();
        button2.setId(2);
        buttons.add(button1);
        buttons.add(button2);

        source.insertNewZone("test zone", buttons);

        verify(database).insert(Zone.TABLE_ZONE, null, values);
        verify(database, times(2)).insert(eq(ZoneButtonLink.TABLE_ZONE_BUTTON_LINK),
                anyString(), any(ContentValues.class));
    }

    @Test(expected = RuntimeException.class)
    public void test_insertZone_nullCommands() {
        source.insertNewZone("test zone", null);
    }

    @Test(expected = RuntimeException.class)
    public void test_insertZone_blankCommands() {
        source.insertNewZone("test zone", new ArrayList<Button>());
    }

    @Test
    public void test_deleteZone() {
        when(database.delete("zone", "_id = 1", null)).thenReturn(2);
        when(database.delete("zone_button_link", "zone_id = 1", null)).thenReturn(3);

        assertEquals(5, source.deleteZone(1));
    }

    @Test
    public void test_findZones() {
        MatrixCursor cursor = new MatrixCursor(new String[] {
                "zone_id", "zone_name", "button_id", "button_name", "command_id", "command_name",
                "gateway_id", "number", "command_type_id", "controller_number", "command_type_name",
                "base_serial_on_code", "base_serial_off_code", "activate_controller_selection"
        });

        cursor.addRow(new String[] {"1", "zone 1", "1", "button 1", "1", "command 1", "1", "1", "1", "1", "type 1", "^A", "^B", "0"});
        cursor.addRow(new String[] {"1", "zone 1", "2", "button 2", "1", "command 1", "1", "1", "1", "1", "type 1", "^A", "^B", "0"});
        cursor.addRow(new String[] {"1", "zone 1", "2", "button 2", "2", "command 2", "1", "1", "1", "1", "type 1", "^A", "^B", "0"});
        cursor.addRow(new String[] {"1", "zone 1", "2", "button 2", "3", "command 3", "1", "1", "1", "1", "type 1", "^A", "^B", "0"});
        cursor.addRow(new String[] {"2", "zone 2", "3", "button 3", "3", "command 3", "1", "1", "1", "1", "type 1", "^A", "^B", "0"});
        cursor.addRow(new String[] {"2", "zone 2", "3", "button 3", "4", "command 4", "1", "1", "1", "1", "type 1", "^A", "^B", "0"});

        when(database.rawQuery(anyString(), any(String[].class))).thenReturn(cursor);

        Gateway gateway = new Gateway();
        gateway.setId(1);
        List<Zone> zones = source.findZones(gateway);

        assertEquals(2, zones.size());

        assertEquals("zone 1", zones.get(0).getName());
        assertEquals(2, zones.get(0).getButtons().size());
        assertEquals("button 1", zones.get(0).getButtons().get(0).getName());
        assertEquals("button 2", zones.get(0).getButtons().get(1).getName());

        assertEquals("zone 2", zones.get(1).getName());
        assertEquals(1, zones.get(1).getButtons().size());
        assertEquals("button 3", zones.get(1).getButtons().get(0).getName());

        assertEquals(1, zones.get(0).getButtons().get(0).getCommands().size());
        assertEquals("command 1", zones.get(0).getButtons().get(0).getCommands().get(0).getName());

        assertEquals(3, zones.get(0).getButtons().get(1).getCommands().size());
        assertEquals("command 1", zones.get(0).getButtons().get(1).getCommands().get(0).getName());
        assertEquals("command 2", zones.get(0).getButtons().get(1).getCommands().get(1).getName());
        assertEquals("command 3", zones.get(0).getButtons().get(1).getCommands().get(2).getName());

        assertEquals(2, zones.get(1).getButtons().get(0).getCommands().size());
        assertEquals("command 3", zones.get(1).getButtons().get(0).getCommands().get(0).getName());
        assertEquals("command 4", zones.get(1).getButtons().get(0).getCommands().get(1).getName());

        assertTrue(cursor.isClosed());
    }

    @Test
    public void test_findZones_noRows() {
        MatrixCursor cursor = new MatrixCursor(new String[] {
                "zone_id", "zone_name", "button_id", "button_name", "command_id", "command_name",
                "gateway_id", "number", "command_type_id", "controller_number", "command_type_name",
                "base_serial_on_code", "base_serial_off_code", "activate_controller_selection"
        });

        when(database.rawQuery(anyString(), any(String[].class))).thenReturn(cursor);
        List<Zone> zones = source.findZones(1);

        assertEquals(0, zones.size());
        assertTrue(cursor.isClosed());
    }

    @Test
    public void test_findZones_nullCursor() {
        when(database.rawQuery(anyString(), any(String[].class))).thenReturn(null);
        List<Zone> zones = source.findZones(1);

        assertEquals(0, zones.size());
    }

}