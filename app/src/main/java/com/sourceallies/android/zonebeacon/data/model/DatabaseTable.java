package com.sourceallies.android.zonebeacon.data.model;

import android.database.Cursor;

public interface DatabaseTable {

    String getCreateStatement();

    String getTableName();

    String[] getIndexStatements();

    String[] getDefaultDataStatements();

    void fillFromCursor(Cursor cursor);

}
