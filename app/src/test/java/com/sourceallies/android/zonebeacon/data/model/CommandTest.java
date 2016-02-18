package com.sourceallies.android.zonebeacon.data.model;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class CommandTest extends DatabaseTableTest {

    private Command command;

    @Before
    public void setUp() {
        command = new Command();
    }

    @Test
    public void test_getTableName() {
        assertEquals("command", command.getTableName());
    }

    @Test
    public void test_indexes() {
        assertEquals(2, command.getIndexStatements().length);

        assertEquals("create index if not exists gateway_id_index on command (gateway_id);",
                command.getIndexStatements()[0]);
        assertEquals("create index if not exists command_type_id_index on command (command_type_id);",
                command.getIndexStatements()[1]);
    }

    @Test
    public void test_defaults() {
        assertEquals(0, command.getDefaultDataStatements().length);
    }

    @Test
    public void test_createTable() {
        assertEquals("create table if not exists command (_id integer primary key autoincrement, " +
                "name varchar(255) not null, gateway_id integer not null, number integer not null, " +
                "command_type_id integer not null, controller_number integer);",
                command.getCreateStatement());
    }

    @Override
    public DatabaseTable getDatabaseTable() {
        return command;
    }

}