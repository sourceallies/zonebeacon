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

    @Override
    public DatabaseTable getDatabaseTable() {
        return type;
    }

}