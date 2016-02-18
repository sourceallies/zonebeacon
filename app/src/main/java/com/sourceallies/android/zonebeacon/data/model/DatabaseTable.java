package com.sourceallies.android.zonebeacon.data.model;

public interface DatabaseTable {

    String getCreateStatement();
    String getTableName();
    String[] getIndexStatements();
    String[] getDefaultDataStatements();

}
