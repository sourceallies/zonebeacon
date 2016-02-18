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

    @Override
    public DatabaseTable getDatabaseTable() {
        return gateway;
    }
}