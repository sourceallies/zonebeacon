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

import android.database.Cursor;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class ButtonTest extends DatabaseTableTest {

    private Button button;

    @Before
    public void setUp() {
        button = new Button();
    }

    @Test
    public void test_getTableName() {
        assertEquals("button", button.getTableName());
    }

    @Test
    public void test_indexes() {
        assertEquals(0, button.getIndexStatements().length);
    }

    @Test
    public void test_createTable() {
        assertEquals("create table if not exists button (_id integer primary key autoincrement, " +
                "name varchar(255) not null);", button.getCreateStatement());
    }

    @Test
    public void test_defaults() {
        assertEquals(0, button.getDefaultDataStatements().length);
    }

    @Test
    public void test_fillFromCursor() {
        setupMockCursor(Button.ALL_COLUMNS);
        button.fillFromCursor(cursor);

        assertColumnFilled(button.getId());
        assertColumnFilled(button.getName());
    }

    @Test
    public void test_getCommands() {
        List<Command> commands = new ArrayList<>();
        button.setCommands(commands);
        assertEquals(commands, button.getCommands());
    }

    @Test
    public void test_toString() {
        button.setName("Test Button");
        assertEquals("Test Button", button.toString());
    }

    @Override
    public DatabaseTable getDatabaseTable() {
        return button;
    }

}