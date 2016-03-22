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

public class CommandType implements DatabaseTable {

    public static final String TABLE_COMMAND_TYPE = "command_type";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_BASE_SERIAL_ON_CODE = "base_serial_on_code";
    public static final String COLUMN_BASE_SERIAL_OFF_CODE = "base_serial_off_code";
    public static final String COLUMN_SYSTEM_TYPE_ID = "system_type_id";
    public static final String COLUMN_ACTIVATE_CONTROLLER_SELECTION = "activate_controller_selection";
    public static final String COLUMN_SHOWN_IN_COMMAND_LIST = "shown_in_command_list";

    public static final String[] ALL_COLUMNS = {
            COLUMN_ID,
            COLUMN_NAME,
            COLUMN_BASE_SERIAL_ON_CODE,
            COLUMN_BASE_SERIAL_OFF_CODE,
            COLUMN_SYSTEM_TYPE_ID,
            COLUMN_ACTIVATE_CONTROLLER_SELECTION,
            COLUMN_SHOWN_IN_COMMAND_LIST
    };

    private static final String DATABASE_CREATE = "create table if not exists " +
            TABLE_COMMAND_TYPE + " (" +
            COLUMN_ID + " integer primary key autoincrement, " +
            COLUMN_NAME + " varchar(255) not null, " +
            COLUMN_BASE_SERIAL_ON_CODE + " varchar(255) not null, " +
            COLUMN_BASE_SERIAL_OFF_CODE + " varchar(255) not null, " +
            COLUMN_SYSTEM_TYPE_ID + " integer not null, " +
            COLUMN_ACTIVATE_CONTROLLER_SELECTION + " integer not null, " +
            COLUMN_SHOWN_IN_COMMAND_LIST + " integer not null" +
            ");";

    private static final String[] INDEXES = {
            "create index if not exists command_type_system_type_id_index on " + TABLE_COMMAND_TYPE +
                    " (" + COLUMN_SYSTEM_TYPE_ID + ");"
    };

    private static final String[] DEFAULTS = {										// Command Examples
            "1, 1, 'Single MCP - Load/Relay', '^A%nnn', '^B%nnn', 0, 1",					// ^A001 or ^B001
            "2, 1, 'Single MCP - Switch', '^S%nnn', '^S%nnn', 0, 1",						// ^S001
            "3, 1, 'Single MCP - Scene', '^C%nnn', '^D%nnn', 0, 1",							// ^C001 or ^D001
            "4, 1, 'Multi MCP - Load/Relay', '^a%s%nnn', '^b%s%nnn', 1, 1",					// ^a1001 or ^b1001 (^<base code> + <controller selection (1-9)> + <load number (001-999)>)
            "5, 1, 'Multi MCP - Switch', '^s%s%nnn', '^s%s%nnn', 1, 1",						// ^s1001 (^s<controller selection (1-9)> + <load number (001-999)>)
            "6, 1, 'Multi MCP - Scene', '^c%s%nnn', '^d%s%nnn', 1, 1",						// ^c1001 or ^d1001 (^<base code> + <controller selection (1-9)> + <load number (001-999)>)
            "7, 1, 'Single MCP - Brightness', '^E%nnn%ll00', '^E%nnn0000', 0, 0",			// ^E0019000 (^<base code> + <load number (001-999)> + <level (01-99)> + <pulse width (00 for us)>)
            "8, 1, 'Multi MCP - Brightness', '^e%s%nnn%ll00', '^e%s%nnn0000', 1, 0"			// ^e10019000 (^<base code> + <controller selection (1 char)> + <load number (3 chars)> + <level (01-99)> + <pulse width (00 for us)>)
    };

    @Setter
    @Getter
    private long id;

    @Setter
    @Getter
    private String name;

    @Setter
    @Getter
    private String baseSerialOnCode;

    @Setter
    @Getter
    private String baseSerialOffCode;

    @Setter
    @Getter
    private long systemTypeId;

    @Setter
    @Getter
    private boolean activateControllerSelection;

    @Setter
    @Getter
    private boolean shownInCommandList;

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
                    COLUMN_SYSTEM_TYPE_ID + "', '" + COLUMN_NAME + "', '" + COLUMN_BASE_SERIAL_ON_CODE +
                    "', '" + COLUMN_BASE_SERIAL_OFF_CODE + "', '" + COLUMN_ACTIVATE_CONTROLLER_SELECTION +
                    "', '" + COLUMN_SHOWN_IN_COMMAND_LIST +
                    "') VALUES (" + DEFAULTS[i] + ");";
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
            } else if (column.equals(COLUMN_BASE_SERIAL_ON_CODE)) {
                setBaseSerialOnCode(cursor.getString(i));
            } else if (column.equals(COLUMN_BASE_SERIAL_OFF_CODE)) {
                setBaseSerialOffCode(cursor.getString(i));
            } else if (column.equals(COLUMN_SYSTEM_TYPE_ID)) {
                setSystemTypeId(cursor.getLong(i));
            } else if (column.equals(COLUMN_ACTIVATE_CONTROLLER_SELECTION)) {
                setActivateControllerSelection(cursor.getInt(i) == 1);
            } else if (column.endsWith(COLUMN_SHOWN_IN_COMMAND_LIST)) {
                setShownInCommandList(cursor.getInt(i) == 1);
            }
        }
    }
}
