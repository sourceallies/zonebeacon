package com.sourceallies.android.zonebeacon.data.model;

import android.database.Cursor;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

/**
 * Model for holding multiple buttons that can be executed at a single time.
 */
public class Zone implements DatabaseTable {

    public static final String TABLE_ZONE = "zone";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_NAME = "name";

    public static final String[] ALL_COLUMNS = {
            COLUMN_ID,
            COLUMN_NAME
    };

    private static final String DATABASE_CREATE = "create table if not exists " +
            TABLE_ZONE + " (" +
            COLUMN_ID + " integer primary key autoincrement, " +
            COLUMN_NAME + " varchar(255) not null" +
            ");";

    @Getter
    @Setter
    private long id;

    @Getter
    @Setter
    private String name;

    @Getter
    @Setter
    private List<Button> buttons;

    @Override
    public String getCreateStatement() {
        return DATABASE_CREATE;
    }

    @Override
    public String getTableName() {
        return TABLE_ZONE;
    }

    @Override
    public String[] getIndexStatements() {
        return new String[0];
    }

    @Override
    public String[] getDefaultDataStatements() {
        return new String[0];
    }

    @Override
    public void fillFromCursor(Cursor cursor) {
        for (int i = 0; i < cursor.getColumnCount(); i++) {
            String column = cursor.getColumnName(i);

            if (column.equals(COLUMN_ID)) {
                setId(cursor.getLong(i));
            } else if (column.equals(COLUMN_NAME)) {
                setName(cursor.getString(i));
            }
        }
    }

    @Override
    public String toString() {
        return getName();
    }
}
