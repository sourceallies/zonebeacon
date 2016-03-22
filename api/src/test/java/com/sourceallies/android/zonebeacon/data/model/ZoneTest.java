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

    @Test
    public void test_toString() {
        zone.setName("Test Zone");
        assertEquals("Test Zone", zone.toString());
    }

    @Override
    public DatabaseTable getDatabaseTable() {
        return zone;
    }

}