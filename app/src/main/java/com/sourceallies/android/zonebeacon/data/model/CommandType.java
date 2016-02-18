package com.sourceallies.android.zonebeacon.data.model;

import lombok.Getter;
import lombok.Setter;

public class CommandType implements DatabaseTable {

    public static final String TABLE_COMMAND_TYPE = "command_type";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_BASE_SERIAL_CODE = "base_serial_code";
    public static final String COLUMN_SYSTEM_TYPE_ID = "system_type_id";

    public static final String[] ALL_COLUMNS = {
            COLUMN_ID,
            COLUMN_NAME,
            COLUMN_BASE_SERIAL_CODE,
            COLUMN_SYSTEM_TYPE_ID
    };

    private static final String DATABASE_CREATE = "create table if not exists " +
            TABLE_COMMAND_TYPE + " (" +
            COLUMN_ID + " integer primary key autoincrement, " +
            COLUMN_NAME + " varchar(255) not null, " +
            COLUMN_BASE_SERIAL_CODE + " varchar(255) not null, " +
            COLUMN_SYSTEM_TYPE_ID + " integer not null" +
            ");";

    private static final String[] INDEXES = {
            "create index if not exists command_type_system_type_id_index on " + TABLE_COMMAND_TYPE +
                    " (" + COLUMN_SYSTEM_TYPE_ID + ");"
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
        return new String[0];
    }

}
