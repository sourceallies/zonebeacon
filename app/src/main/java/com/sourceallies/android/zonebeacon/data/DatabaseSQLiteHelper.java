package com.sourceallies.android.zonebeacon.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.sourceallies.android.zonebeacon.data.model.*;
import com.sourceallies.android.zonebeacon.data.model.Command;

/**
 * Handles creating and updating databases.
 *
 * CHANGELOG BY VERSION:
 * version 1 - create zone, zoneButtonLink, button, buttonCommandLink, command, gateway, systemType
 *             and commandType tables
 */
public class DatabaseSQLiteHelper extends SQLiteOpenHelper {

    private static final String TAG = "DatabaseSQLiteHelper";

    private static final String DATABASE_NAME = "zonebeacon.db";
    private static final int DATABASE_VERSION = 1;

    private DatabaseTable[] tables = {
            new Button(),
            new ButtonCommandLink(),
            new Command(),
            new CommandType(),
            new Gateway(),
            new SystemType(),
            new Zone(),
            new ZoneButtonLink()
    };

    /**
     * Construct a new database helper.
     *
     * @param context the current application context.
     */
    public DatabaseSQLiteHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        for (DatabaseTable table : tables) {
            Log.v(TAG, "creating database table: " + table.getCreateStatement());
            db.execSQL(table.getCreateStatement());

            for (String index : table.getIndexStatements()) {
                db.execSQL(index);
            }

            for (String defaultData : table.getDefaultDataStatements()) {
                db.execSQL(defaultData);
            }
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        // upgrade the database depending on version changes
        // for example, if old version was 1, then we need to execute all changes from 2 through the
        // newest version

        // if (oldVersion < 2) {

        // }

        // if (oldVersion < 3) {

        // }

        // ...
    }

    public void onDrop(SQLiteDatabase db) {
        for (DatabaseTable table : tables) {
            db.execSQL("drop table if exists " + table.getTableName());
        }
    }

}
