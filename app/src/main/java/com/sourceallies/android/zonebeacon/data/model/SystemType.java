package com.sourceallies.android.zonebeacon.data.model;

import lombok.Getter;
import lombok.Setter;

/**
 * Model for holding information on the type of system installed in the gateway.
 */
public class SystemType implements DatabaseTable {

    public static final String TABLE_SYSTEM_TYPE = "system_type";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_VERSION = "version";

    public static final String[] ALL_COLUMNS = {
            COLUMN_ID,
            COLUMN_NAME,
            COLUMN_VERSION
    };

    private static final String DATABASE_CREATE = "create table if not exists " +
            TABLE_SYSTEM_TYPE + " (" +
            COLUMN_ID + " integer primary key autoincrement, " +
            COLUMN_NAME + " varchar(255) not null, " +
            COLUMN_VERSION + " varchar(32)" +
            ");";

    @Setter
    @Getter
    private int id;

    @Setter
    @Getter
    private String name;

    @Setter
    @Getter
    private String version;

    @Override
    public String getCreateStatement() {
        return DATABASE_CREATE;
    }

    @Override
    public String getTableName() {
        return TABLE_SYSTEM_TYPE;
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
