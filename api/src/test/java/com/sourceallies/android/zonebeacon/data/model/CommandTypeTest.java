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
        assertEquals(8, type.getDefaultDataStatements().length);

        assertEquals("INSERT INTO 'command_type' ('_id', 'system_type_id', 'name', 'base_serial_on_code', 'base_serial_off_code', " +
                "'activate_controller_selection', 'shown_in_command_list') VALUES (1, 1, 'Single MCP - Load/Relay', '^A%nnn', '^B%nnn', 0, 1);",
                type.getDefaultDataStatements()[0]);
        assertEquals("INSERT INTO 'command_type' ('_id', 'system_type_id', 'name', 'base_serial_on_code', 'base_serial_off_code', " +
                "'activate_controller_selection', 'shown_in_command_list') VALUES (2, 1, 'Single MCP - Switch', '^S%nnn', '^S%nnn', 0, 1);",
                type.getDefaultDataStatements()[1]);
    }

    @Test
    public void test_createTable() {
        assertEquals("create table if not exists command_type (_id integer primary key " +
                "autoincrement, name varchar(255) not null, base_serial_on_code varchar(255) not " +
                "null, base_serial_off_code varchar(255) not null, system_type_id integer not null, " +
                "activate_controller_selection integer not null, shown_in_command_list integer not null);", type.getCreateStatement());
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
        assertColumnFilled(type.isShownInCommandList());
    }

    @Override
    public DatabaseTable getDatabaseTable() {
        return type;
    }

}