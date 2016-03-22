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
 * Model for linking together zones and buttons.
 */
public class ZoneButtonLink implements DatabaseTable {

    public static final String TABLE_ZONE_BUTTON_LINK = "zone_button_link";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_ZONE_ID = "zone_id";
    public static final String COLUMN_BUTTON_ID = "button_id";

    public static final String[] ALL_COLUMNS = {
            COLUMN_ID,
            COLUMN_ZONE_ID,
            COLUMN_BUTTON_ID
    };

    private static final String DATABASE_CREATE = "create table if not exists " +
            TABLE_ZONE_BUTTON_LINK + " (" +
            COLUMN_ID + " integer primary key autoincrement, " +
            COLUMN_ZONE_ID + " integer not null, " +
            COLUMN_BUTTON_ID + " integer not null" +
            ");";

    private static final String[] INDEXES = {
            "create index if not exists zone_button_button_id_index on " + TABLE_ZONE_BUTTON_LINK +
                    " (" + COLUMN_BUTTON_ID + ");",
            "create index if not exists zone_button_zone_id_index on " + TABLE_ZONE_BUTTON_LINK +
                    " (" + COLUMN_ZONE_ID + ");"
    };

    @Setter
    @Getter
    private long id;

    @Setter
    @Getter
    private long zoneId;

    @Setter
    @Getter
    private long buttonId;

    @Override
    public String getCreateStatement() {
        return DATABASE_CREATE;
    }

    @Override
    public String getTableName() {
        return TABLE_ZONE_BUTTON_LINK;
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
            } else if (column.equals(COLUMN_ZONE_ID)) {
                setZoneId(cursor.getLong(i));
            } else if (column.equals(COLUMN_BUTTON_ID)) {
                setButtonId(cursor.getLong(i));
            }
        }
    }

}
