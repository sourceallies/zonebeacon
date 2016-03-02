package com.sourceallies.android.zonebeacon.data.model;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class ButtonCommandLinkTest extends DatabaseTableTest {

    private ButtonCommandLink link;

    @Before
    public void setUp() {
        link = new ButtonCommandLink();
    }

    @Test
    public void test_getTableName() {
        assertEquals("button_command_link", link.getTableName());
    }

    @Test
    public void test_indexes() {
        assertEquals(2, link.getIndexStatements().length);

        assertEquals("create index if not exists button_command_button_id_index on button_command_link " +
                "(button_id);", link.getIndexStatements()[0]);
        assertEquals("create index if not exists button_command_command_id_index on button_command_link " +
                "(command_id);", link.getIndexStatements()[1]);
    }

    @Test
    public void test_createTable() {
        assertEquals("create table if not exists button_command_link (_id integer primary key " +
                "autoincrement, button_id integer not null, command_id integer not null);",
                link.getCreateStatement());
    }

    @Test
    public void test_defaults() {
        assertEquals(0, link.getDefaultDataStatements().length);
    }

    @Test
    public void test_fillFromCursor() {
        setupMockCursor(ButtonCommandLink.ALL_COLUMNS);
        link.fillFromCursor(cursor);

        assertColumnFilled(link.getId());
        assertColumnFilled(link.getButtonId());
        assertColumnFilled(link.getCommandId());
    }

    @Override
    public DatabaseTable getDatabaseTable() {
        return link;
    }

}