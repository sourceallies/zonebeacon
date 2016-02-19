package com.sourceallies.android.zonebeacon.data;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.sourceallies.android.zonebeacon.ZoneBeaconRobolectricSuite;
import com.sourceallies.android.zonebeacon.data.model.Button;
import com.sourceallies.android.zonebeacon.data.model.Gateway;
import com.sourceallies.android.zonebeacon.data.model.Command;
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

        source = new DataSource(database, RuntimeEnvironment.application);
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

    private int getTableCount(String table) {
        Cursor cursor = source.getDatabase().rawQuery("SELECT count(*) FROM " + table, null);
        if (cursor != null && cursor.moveToFirst()) {
            int numGateways = cursor.getInt(0);
            cursor.close();

            return numGateways;
        }

        throw new RuntimeException("Error finding table count");
    }

    private void insertData() throws Exception {
        SQLiteDatabase database = source.getDatabase();
        FixtureLoader loader = new FixtureLoader();
        loader.loadFixturesToDatabase(database);
    }

}