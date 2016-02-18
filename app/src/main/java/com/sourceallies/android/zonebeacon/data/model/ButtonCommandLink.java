package com.sourceallies.android.zonebeacon.data.model;

import lombok.Getter;
import lombok.Setter;

/**
 * Model linking together buttons and commands.
 */
public class ButtonCommandLink implements DatabaseTable {

    public static final String TABLE_BUTTON_COMMAND_LINK = "button_command_link";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_BUTTON_ID = "button_id";
    public static final String COLUMN_COMMAND_ID = "command_id";

    public static final String[] ALL_COLUMNS = {
            COLUMN_ID,
            COLUMN_BUTTON_ID,
            COLUMN_COMMAND_ID
    };

    private static final String DATABASE_CREATE = "create table if not exists " +
            TABLE_BUTTON_COMMAND_LINK + " (" +
            COLUMN_ID + " integer primary key autoincrement, " +
            COLUMN_BUTTON_ID + " integer not null, " +
            COLUMN_COMMAND_ID + " integer not null" +
            ");";

    private static final String[] INDEXES = {
            "create index if not exists button_command_button_id_index on " + TABLE_BUTTON_COMMAND_LINK +
                    " (" + COLUMN_BUTTON_ID + ");",
            "create index if not exists button_command_command_id_index on " + TABLE_BUTTON_COMMAND_LINK +
                    " (" + COLUMN_COMMAND_ID + ");"
    };

    @Setter
    @Getter
    private int id;

    @Setter
    @Getter
    private int buttonId;

    @Setter
    @Getter
    private int commandId;

    @Override
    public String getCreateStatement() {
        return DATABASE_CREATE;
    }

    @Override
    public String getTableName() {
        return TABLE_BUTTON_COMMAND_LINK;
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
