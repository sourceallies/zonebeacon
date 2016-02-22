package com.sourceallies.android.zonebeacon.data.model;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class CommandTypeTest extends DatabaseTableTest {

    private CommandType type;

    @Before
    public void setUp() {
        type = new CommandType();
    }

    @Test
    public void test_getTableName() {
        assertEquals("command_type", type.getTableName());
    }

    @Test
    public void test_indexes() {
        assertEquals(1, type.getIndexStatements().length);

        assertEquals("create index if not exists command_type_system_type_id_index on command_type " +
                "(system_type_id);", type.getIndexStatements()[0]);
    }

    @Test
    public void test_defaults() {
        assertEquals(6, type.getDefaultDataStatements().length);

        assertEquals("INSERT INTO 'command_type' ('_id', 'system_type_id', 'name', 'base_serial_on_code', 'base_serial_off_code', " +
                "'activate_controller_selection') VALUES (1, 1, 'Single MCP - Load/Relay', '^A', '^B', 0);",
                type.getDefaultDataStatements()[0]);
        assertEquals("INSERT INTO 'command_type' ('_id', 'system_type_id', 'name', 'base_serial_on_code', 'base_serial_off_code', " +
                "'activate_controller_selection') VALUES (2, 1, 'Single MCP - Switch', '^S', '^S', 0);",
                type.getDefaultDataStatements()[1]);
    }

    @Test
    public void test_createTable() {
        assertEquals("create table if not exists command_type (_id integer primary key " +
                "autoincrement, name varchar(255) not null, base_serial_on_code varchar(255) not " +
                "null, base_serial_off_code varchar(255) not null, system_type_id integer not null, " +
                "activate_controller_selection integer not null);", type.getCreateStatement());
    }

    @Test
    public void test_fillFromCursor() {
        setupMockCursor(CommandType.ALL_COLUMNS);
        type.fillFromCursor(cursor);

        assertColumnFilled(type.getId());
        assertColumnFilled(type.getName());
        assertColumnFilled(type.getBaseSerialOnCode());
        assertColumnFilled(type.getBaseSerialOffCode());
        assertColumnFilled(type.getSystemTypeId());
        assertColumnFilled(type.isActivateControllerSelection());
    }

    @Override
    public DatabaseTable getDatabaseTable() {
        return type;
    }

}