package com.sourceallies.android.zonebeacon.data.model;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class ZoneTest extends DatabaseTableTest {

    private Zone zone;

    @Before
    public void setUp() {
        zone = new Zone();
    }

    @Test
    public void test_getTableName() {
        assertEquals("zone", zone.getTableName());
    }

    @Test
    public void test_indexes() {
        assertEquals(0, zone.getIndexStatements().length);
    }

    @Test
    public void test_defaults() {
        assertEquals(0, zone.getDefaultDataStatements().length);
    }

    @Test
    public void test_createTable() {
        assertEquals("create table if not exists zone (_id integer primary key autoincrement, " +
                "name varchar(255) not null);", zone.getCreateStatement());
    }

    @Test
    public void test_fillFromCursor() {
        setupMockCursor(Zone.ALL_COLUMNS);
        zone.fillFromCursor(cursor);

        assertColumnFilled(zone.getId());
        assertColumnFilled(zone.getName());
    }

    @Test
    public void test_getCommands() {
        List<Button> buttons = new ArrayList<>();
        zone.setButtons(buttons);
        assertEquals(buttons, zone.getButtons());
    }

    @Override
    public DatabaseTable getDatabaseTable() {
        return zone;
    }

}