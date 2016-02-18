package com.sourceallies.android.zonebeacon.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.VisibleForTesting;
import android.util.Log;

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

}
