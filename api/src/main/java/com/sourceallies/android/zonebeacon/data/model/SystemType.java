/*
 * Copyright (C) 2016 Source Allies, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.sourceallies.android.zonebeacon.data.model;

import android.database.Cursor;

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

    private static final String[] DEFAULTS = {
            "1, 'CentraLite Elegance', '1.0'"
    };

    @Setter
    @Getter
    private long id;

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
        String[] defaults = new String[DEFAULTS.length];
        for (int i = 0; i < DEFAULTS.length; i++) {
            defaults[i] = "INSERT INTO '" + TABLE_SYSTEM_TYPE + "' ('" + COLUMN_ID + "', '" +
                    COLUMN_NAME + "', '" + COLUMN_VERSION + "') VALUES (" + DEFAULTS[i] + ");";
        }

        return defaults;
    }

    @Override
    public void fillFromCursor(Cursor cursor) {
        for (int i = 0; i < cursor.getColumnCount(); i++) {
            String column = cursor.getColumnName(i);

            if (column.equals(COLUMN_ID)) {
                setId(cursor.getLong(i));
            } else if (column.equals(COLUMN_NAME)) {
                setName(cursor.getString(i));
            } else if (column.equals(COLUMN_VERSION)) {
                setVersion(cursor.getString(i));
            }
        }
    }

}
