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

package com.sourceallies.android.zonebeacon.data;

import android.database.sqlite.SQLiteDatabase;

import com.sourceallies.android.zonebeacon.ZoneBeaconRobolectricSuite;
import com.sourceallies.android.zonebeacon.data.model.*;
import com.sourceallies.android.zonebeacon.data.model.Command;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.robolectric.RuntimeEnvironment;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

public class DatabaseSQLiteHelperTest extends ZoneBeaconRobolectricSuite {

    private DatabaseSQLiteHelper helper;

    @Mock
    private SQLiteDatabase database;

    @Before
    public void setUp() {
        helper = new DatabaseSQLiteHelper(RuntimeEnvironment.application);
    }

    @Test
    public void test_onCreate() {
        helper.onCreate(database);
        verifyCreateStatement();
    }

    @Test
    public void test_onUpgrade_1to2() {
        helper.onUpgrade(database, 1, 2);
        verify2Upgrade();
    }

    @Test
    public void test_onDrop() {
        helper.onDrop(database);
        verifyDropStatement();
    }

    private void verifyCreateStatement() {
        verify(database).execSQL(new Button().getCreateStatement());
        verify(database).execSQL(new ButtonCommandLink().getCreateStatement());
        verify(database).execSQL(new Command().getCreateStatement());
        verify(database).execSQL(new CommandType().getCreateStatement());
        verify(database).execSQL(new Gateway().getCreateStatement());
        verify(database).execSQL(new SystemType().getCreateStatement());
        verify(database).execSQL(new Zone().getCreateStatement());
        verify(database).execSQL(new ZoneButtonLink().getCreateStatement());
        verify(database).execSQL(new ButtonCommandLink().getIndexStatements()[0]);
        verify(database).execSQL(new ButtonCommandLink().getIndexStatements()[1]);
        verify(database).execSQL(new Command().getIndexStatements()[0]);
        verify(database).execSQL(new Command().getIndexStatements()[1]);
        verify(database).execSQL(new CommandType().getIndexStatements()[0]);
        verify(database).execSQL(new Gateway().getIndexStatements()[0]);
        verify(database).execSQL(new ZoneButtonLink().getIndexStatements()[0]);
        verify(database).execSQL(new ZoneButtonLink().getIndexStatements()[1]);
        verify(database).execSQL(new SystemType().getDefaultDataStatements()[0]);
        verify(database).execSQL(new CommandType().getDefaultDataStatements()[0]);
        verify(database).execSQL(new CommandType().getDefaultDataStatements()[1]);
        verify(database).execSQL(new CommandType().getDefaultDataStatements()[2]);
        verify(database).execSQL(new CommandType().getDefaultDataStatements()[3]);
        verify(database).execSQL(new CommandType().getDefaultDataStatements()[4]);
        verify(database).execSQL(new CommandType().getDefaultDataStatements()[5]);
        verify(database).execSQL(new CommandType().getDefaultDataStatements()[6]);
        verify(database).execSQL(new CommandType().getDefaultDataStatements()[7]);
        verifyNoMoreInteractions(database);
    }

    private void verify2Upgrade() {
        // do nothing for now, should be filled when database needs updated.
    }

    private void verifyDropStatement() {
        verify(database).execSQL("drop table if exists " + Button.TABLE);
        verify(database).execSQL("drop table if exists " + ButtonCommandLink.TABLE);
        verify(database).execSQL("drop table if exists " + Command.TABLE);
        verify(database).execSQL("drop table if exists " + CommandType.TABLE);
        verify(database).execSQL("drop table if exists " + Gateway.TABLE);
        verify(database).execSQL("drop table if exists " + SystemType.TABLE);
        verify(database).execSQL("drop table if exists " + Zone.TABLE);
        verify(database).execSQL("drop table if exists " + ZoneButtonLink.TABLE);
        verifyNoMoreInteractions(database);
    }

}