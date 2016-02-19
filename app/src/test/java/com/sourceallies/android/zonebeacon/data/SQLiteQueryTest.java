package com.sourceallies.android.zonebeacon.data;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.sourceallies.android.zonebeacon.ZoneBeaconRobolectricSuite;
import com.sourceallies.android.zonebeacon.data.model.Gateway;
import com.sourceallies.android.zonebeacon.util.FixtureLoader;

import org.junit.Before;
import org.junit.Test;
import org.robolectric.RuntimeEnvironment;

import java.util.List;

import static org.junit.Assert.*;

public class SQLiteQueryTest extends ZoneBeaconRobolectricSuite {

    private DataSource source;

    @Before
    public void setUp() throws Exception {
        SQLiteDatabase database = SQLiteDatabase.create(null);
        DatabaseSQLiteHelper helper = new DatabaseSQLiteHelper(RuntimeEnvironment.application);
        helper.onCreate(database);

        source = new DataSource(database, RuntimeEnvironment.application);
        insertData();
    }

    @Test
    public void test_databaseCreated() {
        assertNotNull(source.getDatabase());

        int numTables = 0;
        Cursor cursor = source.getDatabase().rawQuery("SELECT count(*) FROM sqlite_master " +
                "WHERE type = 'table' AND name != 'android_metadata' AND name != " +
                "'sqlite_sequence';", null);
        if (cursor != null && cursor.moveToFirst()) {
            numTables = cursor.getInt(0);
            cursor.close();
        }

        assertTrue(numTables > 0);
    }

    @Test
    public void test_insertNewGateway() {
        int originalNum = getTableCount("gateway");
        source.insertNewGateway("Test Gateway", "192.168.1.100", 11000);
        int newGateways = getTableCount("gateway") - originalNum;

        assertEquals(1, newGateways);
    }

    @Test
    public void test_deleteGateway() {
        source.insertNewGateway("Test Gateway", "192.168.1.100", 11000);

        int originalNum = getTableCount("gateway");
        source.deleteGateway(1);
        int newGateways = getTableCount("gateway") - originalNum;

        assertEquals(-1, newGateways);
    }

    @Test
    public void test_findGateways() {
        List<Gateway> gateways = source.findGateways();

        assertEquals(1, gateways.size());
        assertEquals("Gateway 1", gateways.get(0).getName());
    }

    private int getTableCount(String table) {
        Cursor cursor = source.getDatabase().rawQuery("SELECT count(*) FROM " + table, null);
        if (cursor != null && cursor.moveToFirst()) {
            int numGateways = cursor.getInt(0);
            cursor.close();

            return numGateways;
        }

        throw new RuntimeException("Error finding table count");
    }

    private void insertData() throws Exception {
        SQLiteDatabase database = source.getDatabase();
        FixtureLoader loader = new FixtureLoader();
        loader.loadFixturesToDatabase(database);
    }

}