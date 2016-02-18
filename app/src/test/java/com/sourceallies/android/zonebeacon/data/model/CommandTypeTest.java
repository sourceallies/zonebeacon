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
        assertEquals(0, type.getDefaultDataStatements().length);
    }

    @Test
    public void test_createTable() {
        assertEquals("create table if not exists command_type (_id integer primary key " +
                "autoincrement, name varchar(255) not null, base_serial_code varchar(255) not " +
                "null, system_type_id integer not null);", type.getCreateStatement());
    }

    @Override
    public DatabaseTable getDatabaseTable() {
        return type;
    }

}