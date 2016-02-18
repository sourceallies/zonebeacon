package com.sourceallies.android.zonebeacon.data.model;

import lombok.Getter;
import lombok.Setter;

/**
 * Model for holding information for a certain button to use a command.
 */
public class Button implements DatabaseTable {

    public static final String TABLE_BUTTON = "button";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_NAME = "name";

    public static final String[] ALL_COLUMNS = {
            COLUMN_ID,
            COLUMN_NAME
    };

    private static final String DATABASE_CREATE = "create table if not exists " +
            TABLE_BUTTON + " (" +
            COLUMN_ID + " integer primary key autoincrement, " +
            COLUMN_NAME + " varchar(255) not null" +
            ");";

    @Setter
    @Getter
    private int id;

    @Setter
    @Getter
    private String name;

    @Override
    public String getCreateStatement() {
        return DATABASE_CREATE;
    }

    @Override
    public String getTableName() {
        return TABLE_BUTTON;
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
