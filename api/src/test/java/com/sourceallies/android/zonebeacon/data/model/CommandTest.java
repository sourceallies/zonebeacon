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

package com.sourceallies.android.zonebeacon.data.model;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;
import static org.mockito.Mockito.when;

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

    @Test
    public void test_fillFromCursor() {
        setupMockCursor(Command.ALL_COLUMNS);
        when(cursor.getString(5)).thenReturn("1");
        command.fillFromCursor(cursor);

        assertColumnFilled(command.getId());
        assertColumnFilled(command.getName());
        assertColumnFilled(command.getCommandTypeId());
        assertColumnFilled(command.getControllerNumber());
        assertColumnFilled(command.getGatewayId());
        assertColumnFilled(command.getNumber());

        assertNotNull(command.getCommandType());
    }

    @Test
    public void test_fillFromCursor_noControllerNumber() {
        setupMockCursor(Command.ALL_COLUMNS);
        when(cursor.getString(5)).thenReturn(null);
        command.fillFromCursor(cursor);

        assertColumnFilled(command.getId());
        assertColumnFilled(command.getName());
        assertColumnFilled(command.getCommandTypeId());
        assertColumnFilled(command.getGatewayId());
        assertColumnFilled(command.getNumber());

        assertColumnNotFilled(command.getControllerNumber());
    }

    @Test
    public void test_toString() {
        command.setName("Test Command");
        assertEquals("Test Command", command.toString());
    }

    @Override
    public DatabaseTable getDatabaseTable() {
        return command;
    }

}