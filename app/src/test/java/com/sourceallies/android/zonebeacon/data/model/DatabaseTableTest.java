package com.sourceallies.android.zonebeacon.data.model;

import android.database.Cursor;

import com.sourceallies.android.zonebeacon.ZoneBeaconRobolectricSuite;

import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;

import static org.junit.Assert.*;

public abstract class DatabaseTableTest extends ZoneBeaconRobolectricSuite {

    @Mock
    public Cursor cursor;

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

    protected void setupMockCursor(String[] columns) {
        Mockito.when(cursor.getColumnCount()).thenReturn(columns.length);

        for (int i = 0; i < columns.length; i++) {
            Mockito.when(cursor.getColumnName(i)).thenReturn(columns[i]);
        }

        Mockito.when(cursor.getString(Mockito.anyInt())).thenReturn("Test String");
        Mockito.when(cursor.getInt(Mockito.anyInt())).thenReturn(1);
        Mockito.when(cursor.getLong(Mockito.anyInt())).thenReturn(1L);
    }

    protected void assertColumnFilled(String string) {
        assertEquals("Test String", string);
    }

    protected void assertColumnFilled(int integer) {
        assertEquals(1, integer);
    }

    protected void assertColumnFilled(Integer integer) {
        if (integer != null) {
            assertEquals(1, (int) integer);
        }
    }

    protected void assertColumnFilled(long l) {
        assertEquals(1, l);
    }

    protected void assertColumnFilled(boolean bool) {
        assertTrue(bool);
    }

}