package com.sourceallies.android.zonebeacon.data.model;

import android.database.Cursor;

import lombok.Getter;
import lombok.Setter;

/**
 * Model for holding information on the gateway such as IP and port number.
 */
public class Gateway implements DatabaseTable {

    public static final String TABLE_GATEWAY = "gateway";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_IP_ADDRESS = "ip_address";
    public static final String COLUMN_PORT_NUMBER = "port_number";
    public static final String COLUMN_SYSTEM_TYPE_ID = "system_type_id";

    public static final String[] ALL_COLUMNS = {
            COLUMN_ID,
            COLUMN_NAME,
            COLUMN_IP_ADDRESS,
            COLUMN_PORT_NUMBER,
            COLUMN_SYSTEM_TYPE_ID
    };

    private static final String DATABASE_CREATE = "create table if not exists " +
            TABLE_GATEWAY + " (" +
            COLUMN_ID + " integer primary key autoincrement, " +
            COLUMN_NAME + " varchar(255) not null, " +
            COLUMN_IP_ADDRESS + " varchar(255) not null, " +
            COLUMN_PORT_NUMBER + " integer not null, " +
            COLUMN_SYSTEM_TYPE_ID + " integer not null" +
            ");";

    private static final String[] INDEXES = {
            "create index if not exists gateway_system_type_id_index on " + TABLE_GATEWAY +
                    " (" + COLUMN_SYSTEM_TYPE_ID + ");"
    };

    @Setter
    @Getter
    private long id;

    @Setter
    @Getter
    private String name;

    @Setter
    @Getter
    private String ipAddress;

    @Setter
    @Getter
    private int portNumber;

    @Setter
    @Getter
    private int systemTypeId;

    @Override
    public String getCreateStatement() {
        return DATABASE_CREATE;
    }

    @Override
    public String getTableName() {
        return TABLE_GATEWAY;
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
            } else if (column.equals(COLUMN_IP_ADDRESS)) {
                setIpAddress(cursor.getString(i));
            } else if (column.equals(COLUMN_PORT_NUMBER)) {
                setPortNumber(cursor.getInt(i));
            } else if (column.equals(COLUMN_SYSTEM_TYPE_ID)) {
                setSystemTypeId(cursor.getInt(i));
            }
        }
    }
}
