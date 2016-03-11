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

public class SystemTypeTest extends DatabaseTableTest {

    private SystemType type;

    @Before
    public void setUp() {
        type = new SystemType();
    }

    @Test
    public void test_getTableName() {
        assertEquals("system_type", type.getTableName());
    }

    @Test
    public void test_indexes() {
        assertEquals(0, type.getIndexStatements().length);
    }

    @Test
    public void test_defaults() {
        assertEquals(1, type.getDefaultDataStatements().length);
        assertEquals("INSERT INTO 'system_type' ('_id', 'name', 'version') VALUES (1, 'CentraLite Elegance', '1.0');",
                type.getDefaultDataStatements()[0]);
    }

    @Test
    public void test_createTable() {
        assertEquals("create table if not exists system_type (_id integer primary key " +
                "autoincrement, name varchar(255) not null, version varchar(32));",
                type.getCreateStatement());
    }

    @Test
    public void test_fillFromCursor() {
        setupMockCursor(SystemType.ALL_COLUMNS);
        type.fillFromCursor(cursor);

        assertColumnFilled(type.getId());
        assertColumnFilled(type.getName());
        assertColumnFilled(type.getVersion());
    }

    @Override
    public DatabaseTable getDatabaseTable() {
        return type;
    }

}