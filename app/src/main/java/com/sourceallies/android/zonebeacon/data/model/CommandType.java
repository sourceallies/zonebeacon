package com.sourceallies.android.zonebeacon.data.model;

import android.database.Cursor;

import lombok.Getter;
import lombok.Setter;

public class CommandType implements DatabaseTable {

    public static final String TABLE_COMMAND_TYPE = "command_type";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_BASE_SERIAL_CODE = "base_serial_code";
    public static final String COLUMN_SYSTEM_TYPE_ID = "system_type_id";
    public static final String COLUMN_ACTIVATE_CONTROLLER_SELECTION = "activate_controller_selection";

    public static final String[] ALL_COLUMNS = {
            COLUMN_ID,
            COLUMN_NAME,
            COLUMN_BASE_SERIAL_CODE,
            COLUMN_SYSTEM_TYPE_ID,
            COLUMN_ACTIVATE_CONTROLLER_SELECTION
    };

    private static final String DATABASE_CREATE = "create table if not exists " +
            TABLE_COMMAND_TYPE + " (" +
            COLUMN_ID + " integer primary key autoincrement, " +
            COLUMN_NAME + " varchar(255) not null, " +
            COLUMN_BASE_SERIAL_CODE + " varchar(255) not null, " +
            COLUMN_SYSTEM_TYPE_ID + " integer not null, " +
            COLUMN_ACTIVATE_CONTROLLER_SELECTION + " integer not null" +
            ");";

    private static final String[] INDEXES = {
            "create index if not exists command_type_system_type_id_index on " + TABLE_COMMAND_TYPE +
                    " (" + COLUMN_SYSTEM_TYPE_ID + ");"
    };

    private static final String[] DEFAULTS = {
            "1, 1, 'Single MCP - Load/Relay', '^A', 0",
            "2, 1, 'Single MCP - Switch', '^S', 0",
            "3, 1, 'Single MCP - Scene', '^C', 0",
            "4, 1, 'Multi MCP - Load/Relay', '^a', 1",
            "5, 1, 'Multi MCP - Switch', '^s', 1",
            "6, 1, 'Multi MCP - Scene', '^c', 1"
    };

    @Setter
    @Getter
    private int id;

    @Setter
    @Getter
    private String name;

    @Setter
    @Getter
    private String baseSerialCode;

    @Setter
    @Getter
    private int systemTypeId;

    @Setter
    @Getter
    private boolean activateControllerSelection;

    @Override
    public String getCreateStatement() {
        return DATABASE_CREATE;
    }

    @Override
    public String getTableName() {
        return TABLE_COMMAND_TYPE;
    }

    @Override
    public String[] getIndexStatements() {
        return INDEXES;
    }

    @Override
    public String[] getDefaultDataStatements() {
        String[] defaults = new String[DEFAULTS.length];
        for (int i = 0; i < DEFAULTS.length; i++) {
            defaults[i] = "INSERT INTO '" + TABLE_COMMAND_TYPE + "' ('" + COLUMN_ID + "', '" +
                    COLUMN_SYSTEM_TYPE_ID + "', '" + COLUMN_NAME + "', '" + COLUMN_BASE_SERIAL_CODE +
                    "', '" + COLUMN_ACTIVATE_CONTROLLER_SELECTION + "') VALUES (" + DEFAULTS[i] + ");";
        }

        return defaults;
    }

    @Override
    public void fillFromCursor(Cursor cursor) {
        for (int i = 0; i < cursor.getColumnCount(); i++) {
            String column = cursor.getColumnName(i);

            if (column.equals(COLUMN_ID)) {
                id = cursor.getInt(i);
            } else if (column.equals(COLUMN_NAME)) {
                name = cursor.getString(i);
            } else if (column.equals(COLUMN_BASE_SERIAL_CODE)) {
                baseSerialCode = cursor.getString(i);
            } else if (column.equals(COLUMN_SYSTEM_TYPE_ID)) {
                systemTypeId = cursor.getInt(i);
            } else if (column.equals(COLUMN_ACTIVATE_CONTROLLER_SELECTION)) {
                activateControllerSelection = cursor.getInt(i) == 1;
            }
        }
    }
}
