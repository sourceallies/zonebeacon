package com.sourceallies.android.zonebeacon.data.model;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class ZoneButtonLinkTest extends DatabaseTableTest {

    private ZoneButtonLink link;

    @Before
    public void setUp() {
        link = new ZoneButtonLink();
    }

    @Test
    public void test_getTableName() {
        assertEquals("zone_button_link", link.getTableName());
    }

    @Test
    public void test_indexes() {
        assertEquals(2, link.getIndexStatements().length);

        assertEquals("create index if not exists zone_button_button_id_index on zone_button_link " +
                "(button_id);", link.getIndexStatements()[0]);
        assertEquals("create index if not exists zone_button_zone_id_index on zone_button_link " +
                "(zone_id);", link.getIndexStatements()[1]);
    }

    @Test
    public void test_defaults() {
        assertEquals(0, link.getDefaultDataStatements().length);
    }

    @Test
    public void test_createTable() {
        assertEquals("create table if not exists zone_button_link (_id integer primary key " +
                "autoincrement, zone_id integer not null, button_id integer not null);",
                link.getCreateStatement());
    }

    @Override
    public DatabaseTable getDatabaseTable() {
        return link;
    }

}