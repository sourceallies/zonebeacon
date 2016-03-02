package com.sourceallies.android.zonebeacon.data.model;

import android.database.Cursor;
import android.os.Handler;

import lombok.Getter;
import lombok.Setter;

/**
 * Model for holding information on executing commands to activate or deactivate lights.
 */
public class Command implements DatabaseTable {

    public static final String TABLE_COMMAND = "command";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_GATEWAY_ID = "gateway_id";
    public static final String COLUMN_NUMBER = "number";
    public static final String COLUMN_COMMAND_TYPE_ID = "command_type_id";
    public static final String COLUMN_CONTROLLER_NUMBER = "controller_number";

    public static final String[] ALL_COLUMNS = {
            COLUMN_ID,
            COLUMN_NAME,
            COLUMN_GATEWAY_ID,
            COLUMN_NUMBER,
            COLUMN_COMMAND_TYPE_ID,
            COLUMN_CONTROLLER_NUMBER
    };

    private static final String DATABASE_CREATE = "create table if not exists " +
            TABLE_COMMAND + " (" +
            COLUMN_ID + " integer primary key autoincrement, " +
            COLUMN_NAME + " varchar(255) not null, " +
            COLUMN_GATEWAY_ID + " integer not null, " +
            COLUMN_NUMBER + " integer not null, " +
            COLUMN_COMMAND_TYPE_ID + " integer not null, " +
            COLUMN_CONTROLLER_NUMBER + " integer" +
            ");";

    private static final String[] INDEXES = {
            "create index if not exists gateway_id_index on " + TABLE_COMMAND +
                    " (" + COLUMN_GATEWAY_ID + ");",
            "create index if not exists command_type_id_index on " + TABLE_COMMAND +
                    " (" + COLUMN_COMMAND_TYPE_ID + ");"
    };

    @Setter
    @Getter
    private long id;

    @Setter
    @Getter
    private String name;

    @Setter
    @Getter
    private long gatewayId;

    @Setter
    @Getter
    private int number;

    @Setter
    @Getter
    private long commandTypeId;

    @Setter
    @Getter
    private Integer controllerNumber;

    @Override
    public String getCreateStatement() {
        return DATABASE_CREATE;
    }

    @Override
    public String getTableName() {
        return TABLE_COMMAND;
    }

    @Override
    public String[] getIndexStatements() {
        return INDEXES;
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
            } else if (column.equals(COLUMN_GATEWAY_ID)) {
                setGatewayId(cursor.getLong(i));
            } else if (column.equals(COLUMN_NUMBER)) {
                setNumber(cursor.getInt(i));
            } else if (column.equals(COLUMN_COMMAND_TYPE_ID)) {
                setCommandTypeId(cursor.getLong(i));
            } else if (column.equals(COLUMN_CONTROLLER_NUMBER)) {
                try {
                    Integer controllerNumber = Integer.parseInt(cursor.getString(i));
                    setControllerNumber(controllerNumber);
                } catch (Exception e) {
                    setControllerNumber(null);
                }
            }
        }
    }

}
