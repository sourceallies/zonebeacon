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

public class GatewayTest extends DatabaseTableTest {

    private Gateway gateway;

    @Before
    public void setUp() {
        gateway = new Gateway();
    }

    @Test
    public void test_getTableName() {
        assertEquals("gateway", gateway.getTableName());
    }

    @Test
    public void test_indexes() {
        assertEquals(1, gateway.getIndexStatements().length);

        assertEquals("create index if not exists gateway_system_type_id_index on gateway " +
                "(system_type_id);", gateway.getIndexStatements()[0]);
    }

    @Test
    public void test_default() {
        assertEquals(0, gateway.getDefaultDataStatements().length);
    }

    @Test
    public void test_createTable() {
        assertEquals("create table if not exists gateway (_id integer primary key autoincrement, " +
                "name varchar(255) not null, ip_address varchar(255) not null, port_number " +
                "integer not null, system_type_id integer not null);", gateway.getCreateStatement());
    }

    @Test
    public void test_fillFromCursor() {
        setupMockCursor(Gateway.ALL_COLUMNS);
        gateway.fillFromCursor(cursor);

        assertColumnFilled(gateway.getId());
        assertColumnFilled(gateway.getName());
        assertColumnFilled(gateway.getSystemTypeId());
        assertColumnFilled(gateway.getIpAddress());
        assertColumnFilled(gateway.getPortNumber());
    }

    @Override
    public DatabaseTable getDatabaseTable() {
        return gateway;
    }
}