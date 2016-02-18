package com.sourceallies.android.zonebeacon.data.model;

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
    private int id;

    @Getter
    @Setter
    private String name;

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

}
