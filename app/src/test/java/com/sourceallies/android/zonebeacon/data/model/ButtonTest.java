package com.sourceallies.android.zonebeacon.data.model;

import org.junit.Before;
import org.junit.Test;

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

    @Override
    public DatabaseTable getDatabaseTable() {
        return button;
    }
}