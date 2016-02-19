package com.sourceallies.android.zonebeacon.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.VisibleForTesting;
import android.util.Log;

import com.sourceallies.android.zonebeacon.data.model.Gateway;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Handles interactions with database models.
 */
public class DataSource {

    private static final String TAG = "DataSource";
    private static volatile DataSource instance;

    private Context context;
    private SQLiteDatabase database;
    private DatabaseSQLiteHelper dbHelper;
    private AtomicInteger openCounter = new AtomicInteger();

    /**
     * Gets a new instance of the DataSource.
     *
     * @param context the current application instance.
     * @return the data source.
     */
    public static DataSource getInstance(Context context) {
        if (instance == null) {
            instance = new DataSource(context);
        }

        return instance;
    }

    private DataSource(Context context) {
        this.dbHelper = new DatabaseSQLiteHelper(context);
        this.context = context;
    }

    @VisibleForTesting
    protected DataSource(DatabaseSQLiteHelper helper, Context context) {
        this.dbHelper = helper;
        this.context = context;
    }

    @VisibleForTesting
    protected DataSource(SQLiteDatabase database, Context context) {
        this.database = database;
        this.context = context;
    }

    /**
     * Opens the database.
     */
    public synchronized void open() {
        Log.v(TAG, "current open counter for opening: " + openCounter);
        if (openCounter.incrementAndGet() == 1) {
            Log.v(TAG, "getting writable database");
            database = dbHelper.getWritableDatabase();
        }
    }

    /**
     * Closes the database.
     */
    public synchronized void close() {
        Log.v(TAG, "current open counter for closing: " + openCounter);
        if (openCounter.decrementAndGet() == 0) {
            Log.v(TAG, "closing writable database");
            dbHelper.close();
        }
    }

    @VisibleForTesting
    protected SQLiteDatabase getDatabase() {
        return database;
    }

    /**
     * Begins a bulk transaction on the database.
     */
    public void beginTransaction() {
        database.beginTransaction();
    }

    /**
     * Executes a raw sql statement on the database. Can be used in conjunction with
     * beginTransaction and endTransaction if bulk.
     *
     * @param sql the sql statement.
     */
    public void execSql(String sql) {
        database.execSQL(sql);
    }

    /**
     * Execute a raw sql query on the database.
     *
     * @param sql the sql statement
     * @return cursor for the data
     */
    public Cursor rawQuery(String sql) {
        return database.rawQuery(sql, null);
    }

    /**
     * Sets the transaction into a successful state so that it can be committed to the database.
     * Should be used in conjunction with beginTransaction() and endTransaction().
     */
    public void setTransactionSuccessful() {
        database.setTransactionSuccessful();
    }

    /**
     * Ends a bulk transaction on the database.
     */
    public void endTransaction() {
        database.endTransaction();
    }


    /*
            Methods for manipulating the database
     */



    /**
     * Insert a new gateway into the database
     *
     * @param name Gateway name
     * @param ip ip address for the gateway
     * @param port port number for the gateway
     * @return id of the inserted row
     */
    public long insertNewGateway(String name, String ip, int port) {
        ContentValues values = new ContentValues();
        values.put(Gateway.COLUMN_NAME, name);
        values.put(Gateway.COLUMN_IP_ADDRESS, ip);
        values.put(Gateway.COLUMN_PORT_NUMBER, port);
        values.put(Gateway.COLUMN_SYSTEM_TYPE_ID, 1); // just one for now. Since there is only elegance.

        return database.insert(Gateway.TABLE_GATEWAY, null, values);
    }

    /**
     * Deletes a gateway from the database.
     *
     * @param gatewayId id of the gateway to delete
     * @return the number of items deleted with the statement.
     */
    public int deleteGateway(int gatewayId) {
        return database.delete(Gateway.TABLE_GATEWAY, "_id = " + gatewayId, null);
    }

    /**
     * Get a list of all the gateways in the database.
     *
     * @return all the gateways in the database
     */
    public List<Gateway> findGateways() {
        Cursor cursor = rawQuery("SELECT * from gateway");
        List<Gateway> gateways = new ArrayList<>();

        if (cursor.moveToFirst()) {
            do {
                Gateway gateway = new Gateway();
                gateway.fillFromCursor(cursor);

                gateways.add(gateway);
            } while (cursor.moveToNext());
        }

        cursor.close();

        return gateways;
    }

}
