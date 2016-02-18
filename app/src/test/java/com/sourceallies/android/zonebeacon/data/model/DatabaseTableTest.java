package com.sourceallies.android.zonebeacon.data.model;

import org.junit.Test;

import static org.junit.Assert.*;

public abstract class DatabaseTableTest {

    public abstract DatabaseTable getDatabaseTable();

    @Test
    public void test_createNotNull() {
        assertNotNull(getDatabaseTable().getCreateStatement());
    }

    @Test
    public void test_indexesNotNull() {
        assertNotNull(getDatabaseTable().getIndexStatements());
    }

    @Test
    public void test_tableNameNotNull() {
        assertNotNull(getDatabaseTable().getTableName());
    }

    @Test
    public void test_defaultDataNotNull() {
        assertNotNull(getDatabaseTable().getDefaultDataStatements());
    }

}