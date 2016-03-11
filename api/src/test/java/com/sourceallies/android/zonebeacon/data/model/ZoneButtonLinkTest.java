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

    @Test
    public void test_fillFromCursor() {
        setupMockCursor(ZoneButtonLink.ALL_COLUMNS);
        link.fillFromCursor(cursor);

        assertColumnFilled(link.getId());
        assertColumnFilled(link.getButtonId());
        assertColumnFilled(link.getZoneId());
    }

    @Override
    public DatabaseTable getDatabaseTable() {
        return link;
    }

}