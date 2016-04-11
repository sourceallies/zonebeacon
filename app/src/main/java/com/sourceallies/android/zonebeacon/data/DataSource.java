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

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.VisibleForTesting;
import android.util.Log;

import com.sourceallies.android.zonebeacon.data.model.Button;
import com.sourceallies.android.zonebeacon.data.model.ButtonCommandLink;
import com.sourceallies.android.zonebeacon.data.model.Command;
import com.sourceallies.android.zonebeacon.data.model.CommandType;
import com.sourceallies.android.zonebeacon.data.model.Gateway;
import com.sourceallies.android.zonebeacon.data.model.SystemType;
import com.sourceallies.android.zonebeacon.data.model.Zone;
import com.sourceallies.android.zonebeacon.data.model.ZoneButtonLink;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Handles interactions with database models.
 */
public class DataSource {

    private static final String TAG = "DataSource";
    private static volatile DataSource instance;

    private SQLiteDatabase database;
    private DatabaseSQLiteHelper dbHelper;
    private AtomicInteger openCounter = new AtomicInteger();

    /**
     * Gets a new instance of the DataSource.
     *
     * @param context the current application instance.
     * @return the data source.
     */
    public static DataSource getInstance(Context context) {
        if (instance == null) {
            instance = new DataSource(context);
        }

        return instance;
    }

    /**
     * Private constructor to force a singleton.
     *
     * @param context Current calling context
     */
    private DataSource(Context context) {
        this.dbHelper = new DatabaseSQLiteHelper(context);
    }

    /**
     * Contructor to help with testing.
     *
     * @param helper Mock of the database helper
     */
    @VisibleForTesting
    protected DataSource(DatabaseSQLiteHelper helper) {
        this.dbHelper = helper;
    }

    /**
     * Constructor to help with testing.
     *
     * @param database Mock of the sqlite database
     */
    @VisibleForTesting
    protected DataSource(SQLiteDatabase database) {
        this.database = database;
    }

    /**
     * Opens the database.
     */
    public synchronized void open() {
        Log.v(TAG, "current open counter for opening: " + openCounter);
        if (openCounter.incrementAndGet() == 1) {
            Log.v(TAG, "getting writable database");
            database = dbHelper.getWritableDatabase();
        }
    }

    /**
     * Closes the database.
     */
    public synchronized void close() {
        Log.v(TAG, "current open counter for closing: " + openCounter);
        if (openCounter.decrementAndGet() == 0) {
            Log.v(TAG, "closing writable database");
            dbHelper.close();
        }
    }

    /**
     * Available to close the database after tests have finished running. Don't call
     * in the production application outside of test code.
     */
    @VisibleForTesting
    public synchronized static void forceCloseImmediate() {
        if (instance != null && instance.openCounter.get() > 0) {
            instance.openCounter.set(0);
            instance.dbHelper.close();
            instance = null;
        }
    }

    /**
     * Get the currently open database
     *
     * @return sqlite database
     */
    @VisibleForTesting
    protected SQLiteDatabase getDatabase() {
        return database;
    }

    /**
     * Begins a bulk transaction on the database.
     */
    public void beginTransaction() {
        database.beginTransaction();
    }

    /**
     * Executes a raw sql statement on the database. Can be used in conjunction with
     * beginTransaction and endTransaction if bulk.
     *
     * @param sql the sql statement.
     */
    public void execSql(String sql) {
        database.execSQL(sql);
    }

    /**
     * Execute a raw sql query on the database.
     *
     * @param sql the sql statement
     * @return cursor for the data
     */
    public Cursor rawQuery(String sql) {
        return database.rawQuery(sql, null);
    }

    /**
     * Sets the transaction into a successful state so that it can be committed to the database.
     * Should be used in conjunction with beginTransaction() and endTransaction().
     */
    public void setTransactionSuccessful() {
        database.setTransactionSuccessful();
    }

    /**
     * Ends a bulk transaction on the database.
     */
    public void endTransaction() {
        database.endTransaction();
    }


    /*
            Methods for manipulating the database
     */


    /**
     * Insert a new gateway into the database
     *
     * @param name Gateway name
     * @param ip   ip address for the gateway
     * @param port port number for the gateway
     * @return id of the inserted row
     */
    public long insertNewGateway(String name, String ip, int port) {
        ContentValues values = new ContentValues();
        values.put(Gateway.COLUMN_NAME, name);
        values.put(Gateway.COLUMN_IP_ADDRESS, ip);
        values.put(Gateway.COLUMN_PORT_NUMBER, port);
        values.put(Gateway.COLUMN_SYSTEM_TYPE_ID, 1); // just one for now. Since there is only elegance.

        return database.insert(Gateway.TABLE, null, values);
    }

    /**
     * Deletes a gateway from the database.
     *
     * @param gatewayId id of the gateway to delete
     * @return the number of items deleted with the statement.
     */
    public int deleteGateway(long gatewayId) {
        List<Zone> zones = findZones(gatewayId);

        for (Zone zone : zones) {
            for (Button button : zone.getButtons()) {
                for (Command command : button.getCommands()) {
                    database.delete(Command.TABLE, Command.COLUMN_ID + " = " + command.getId(), null);
                }

                database.delete(Button.TABLE, Button.COLUMN_ID + " = " + button.getId(), null);
            }

            database.delete(Zone.TABLE, Zone.COLUMN_ID + " = " + zone.getId(), null);
        }

        return database.delete(Gateway.TABLE, Gateway.COLUMN_ID + " = " + gatewayId, null);
    }

    /**
     * Get a list of all the gateways in the database.
     *
     * @return all the gateways in the database
     */
    public List<Gateway> findGateways() {
        Cursor cursor = rawQuery("SELECT * from gateway");
        List<Gateway> gateways = new ArrayList<>();

        if (cursor == null) {
            return gateways;
        }

        if (cursor.moveToFirst()) {
            do {
                Gateway gateway = new Gateway();
                gateway.fillFromCursor(cursor);

                gateways.add(gateway);
            } while (cursor.moveToNext());
        }

        cursor.close();

        return gateways;
    }

    /**
     * Gets the instance of the gateway based on an id.
     *
     * @param id the id of the gateway.
     * @return the gateway.
     */
    public Gateway findGateway(long id) {
        Cursor cursor = rawQuery("SELECT * from gateway where _id = " + id);
        Gateway gateway = new Gateway();

        if (cursor == null) {
            return null;
        }

        if (cursor.moveToFirst()) {
            gateway.fillFromCursor(cursor);
        }

        cursor.close();

        return gateway;
    }

    /**
     * Gets a list of all of the command types in the database for the given gateway.
     *
     * @param gateway the gateway to find command types for.
     * @return a list of command types.
     */
    public List<CommandType> findCommandTypes(Gateway gateway) {
        return findCommandTypes(gateway.getSystemTypeId());
    }

    private List<CommandType> findCommandTypes(long systemTypeId) {
        return findCommandTypes("SELECT * from command_type where system_type_id = "
                + systemTypeId);
    }

    /**
     * Gets a list of command types that should be shown in the UI when a user is inserting a
     * new command.
     *
     * @param gateway the gateway to find command types for.
     * @return a list of command types.
     */
    public List<CommandType> findCommandTypesShownInUI(Gateway gateway) {
        return findCommandTypes(gateway.getSystemTypeId(), true);
    }

    /**
     * Gets a list of command types that should NOT be shown in the UI when a user is inserting a
     * new command (eg. brightness controls).
     *
     * @param gateway the gateway to find command types for.
     * @return a list of command types.
     */
    public List<CommandType> findCommandTypesNotShownInUI(Gateway gateway) {
        return findCommandTypes(gateway.getSystemTypeId(), false);
    }

    private List<CommandType> findCommandTypes(long systemTypeId, boolean shownInList) {
        return findCommandTypes("SELECT * from command_type where system_type_id = "
                + systemTypeId + " AND shown_in_command_list = " + (shownInList ? "1" : "0"));

    }

    private List<CommandType> findCommandTypes(String sqlQuery) {
        Cursor cursor = rawQuery(sqlQuery);
        List<CommandType> types = new ArrayList<>();

        if (cursor == null) {
            return types;
        }

        if (cursor.moveToFirst()) {
            do {
                CommandType type = new CommandType();
                type.fillFromCursor(cursor);

                types.add(type);
            } while (cursor.moveToNext());
        }

        cursor.close();

        return types;
    }

    /**
     * Gets a list of all system types in the database that we can work with.
     *
     * @return the system types.
     */
    public List<SystemType> findSystemTypes() {
        Cursor cursor = rawQuery("SELECT * from system_type");
        List<SystemType> types = new ArrayList<>();

        if (cursor == null) {
            return types;
        }

        if (cursor.moveToFirst()) {
            do {
                SystemType type = new SystemType();
                type.fillFromCursor(cursor);

                types.add(type);
            } while (cursor.moveToNext());
        }

        cursor.close();

        return types;
    }

    /**
     * Inserts a new command into the database.
     *
     * @param name             the name of the command.
     * @param gatewayId        the gateway id.
     * @param number           the number.
     * @param commandType      the command type.
     * @param controllerNumber the controller number.
     * @return the id of the inserted row.
     */
    public long insertNewCommand(String name, int gatewayId, int number, CommandType commandType,
                                 Integer controllerNumber) {
        return insertNewCommand(name, gatewayId, number, commandType.getId(), controllerNumber);
    }

    /**
     * Inserts a new command into the database.
     *
     * @param name             the name of the command.
     * @param gatewayId        the gateway id.
     * @param number           the number.
     * @param commandTypeId    the command type id.
     * @param controllerNumber the controller number.
     * @return the id of the inserted row.
     */
    public long insertNewCommand(String name, long gatewayId, int number, long commandTypeId,
                                 Integer controllerNumber) {
        ContentValues values = new ContentValues(5);
        values.put(Command.COLUMN_NAME, name);
        values.put(Command.COLUMN_GATEWAY_ID, gatewayId);
        values.put(Command.COLUMN_NUMBER, number);
        values.put(Command.COLUMN_COMMAND_TYPE_ID, commandTypeId);
        values.put(Command.COLUMN_CONTROLLER_NUMBER, controllerNumber);

        return database.insert(Command.TABLE, null, values);
    }

    /**
     * Deletes the command from the database. This should be called after deleting buttons
     * associated with this command.
     *
     * @param id the id to delete.
     * @return the number of items deleted.
     */
    public int deleteCommand(long id) {
        return database.delete(Command.TABLE, Command.COLUMN_ID + " = " + id, null);
    }

    /**
     * Finds a list of all commands for a given gateway.
     *
     * @param gateway the gateway to find commands for.
     * @return a list of all commands associated with the gateway.
     */
    public List<Command> findCommands(Gateway gateway) {
        return findCommands(gateway.getId());
    }

    /**
     * Finds a list of all commands for a given gateway.
     *
     * @param gatewayId the id of the gateway to find commands for.
     * @return a list of all commands associated with the gateway.
     */
    public List<Command> findCommands(long gatewayId) {
        Cursor cursor = rawQuery("SELECT * from command where gateway_id = " + gatewayId);
        List<Command> commands = new ArrayList<>();

        if (cursor == null) {
            return commands;
        }

        if (cursor.moveToFirst()) {
            do {
                Command command = new Command();
                command.fillFromCursor(cursor);

                commands.add(command);
            } while (cursor.moveToNext());
        }

        cursor.close();

        return commands;
    }

    /**
     * Inserts a new button into the database. This will also insert links between the button and
     * the commands that are provided.
     *
     * @param name     the name for the button.
     * @param commands the commands to execute when the button is pressed.
     * @return the id of the inserted button.
     */
    public long insertNewButton(String name, List<Command> commands) {
        if (commands == null || commands.size() == 0) {
            throw new RuntimeException("Commands cannot be blank!");
        }

        ContentValues values = new ContentValues(1);
        values.put(Button.COLUMN_NAME, name);

        long buttonId = database.insert(Button.TABLE, null, values);

        if (buttonId != -1) {
            for (Command command : commands) {
                ContentValues v = new ContentValues(2);
                v.put(ButtonCommandLink.COLUMN_COMMAND_ID, command.getId());
                v.put(ButtonCommandLink.COLUMN_BUTTON_ID, buttonId);
                database.insert(ButtonCommandLink.TABLE, null, v);
            }
        }

        return buttonId;
    }

    /**
     * Deletes a button from the database. This will also remove any button-command links that are
     * stored in the database to maintain database integrity.
     *
     * @param id the id of the button to delete.
     * @return the number of rows deleted.
     */
    public int deleteButton(long id) {
        int deleted = database.delete(Button.TABLE, Button.COLUMN_ID + " = " + id, null);
        deleted += database.delete(ButtonCommandLink.TABLE,
                ButtonCommandLink.COLUMN_BUTTON_ID + " = " + id, null);

        return deleted;
    }

    /**
     * Finds the buttons and commands that are associated with a gateway.
     *
     * @param gateway the gateway.
     * @return the list of buttons that are set up for this gateway.
     */
    public List<Button> findButtons(Gateway gateway) {
        return findButtons(gateway.getId());
    }

    /**
     * Finds the buttons and commands that are associated with a gateway.
     *
     * @param gatewayId the gateway id.
     * @return the list of buttons that are set up for this gateway.
     */
    public List<Button> findButtons(long gatewayId) {
        Cursor cursor = rawQuery(
                "SELECT " +
                        "b._id as button_id, " +
                        "b.name as button_name, " +
                        "c._id as command_id, " +
                        "c.name as command_name, " +
                        "c.gateway_id as gateway_id, " +
                        "c.number as number, " +
                        "c.command_type_id as command_type_id, " +
                        "c.controller_number as controller_number, " +
                        "t.name as command_type_name, " +
                        "t.base_serial_on_code as base_serial_on_code, " +
                        "t.base_serial_off_code as base_serial_off_code, " +
                        "t.activate_controller_selection as activate_controller_selection " +
                        "FROM button b " +
                        "JOIN button_command_link bcl " +
                        "ON b._id=bcl.button_id " +
                        "JOIN command c " +
                        "ON c._id=bcl.command_id " +
                        "JOIN command_type t " +
                        "ON c.command_type_id=t._id " +
                        "WHERE c.gateway_id=" + gatewayId + " " +
                        "ORDER BY b._id asc, c._id asc"
        );

        if (cursor == null) {
            return new ArrayList<Button>();
        }

        Map<Long, Button> buttons = new HashMap<>();

        if (cursor.moveToFirst()) {
            do {
                long buttonId = cursor.getLong(0);
                if (buttons.get(buttonId) == null) {
                    Button button = new Button();
                    button.setId(buttonId);
                    button.setCommands(new ArrayList<Command>());
                    button.setName(cursor.getString(1));

                    buttons.put(buttonId, button);
                }

                Button button = buttons.get(buttonId);

                Command command = new Command();
                command.setId(cursor.getLong(2));
                command.setName(cursor.getString(3));
                command.setGatewayId(cursor.getLong(4));
                command.setNumber(cursor.getInt(5));
                command.setCommandTypeId(cursor.getLong(6));

                try {
                    Integer controllerNumber = Integer.parseInt(cursor.getString(7));
                    command.setControllerNumber(controllerNumber);
                } catch (Exception e) {
                    command.setControllerNumber(null);
                }

                CommandType commandType = new CommandType();
                commandType.setId(cursor.getLong(6));
                commandType.setName(cursor.getString(8));
                commandType.setBaseSerialOnCode(cursor.getString(9));
                commandType.setBaseSerialOffCode(cursor.getString(10));
                commandType.setActivateControllerSelection(cursor.getInt(11) == 1);

                command.setCommandType(commandType);

                button.getCommands().add(command);
            } while (cursor.moveToNext());
        }

        cursor.close();

        return new ArrayList<>(buttons.values());
    }

    /**
     * Inserts a new zone into the database. This will also insert links between the zone and
     * the buttons that are provided.
     *
     * @param name    the name for the zone.
     * @param buttons the buttons to execute when the button is pressed. Each button holds a list
     *                of commands.
     * @return the id of the inserted zone.
     */
    public long insertNewZone(String name, List<Button> buttons) {
        if (buttons == null || buttons.size() == 0) {
            throw new RuntimeException("Buttons cannot be blank!");
        }

        ContentValues values = new ContentValues(1);
        values.put(Zone.COLUMN_NAME, name);

        long zoneId = database.insert(Zone.TABLE, null, values);

        if (zoneId != -1) {
            for (Button button : buttons) {
                ContentValues v = new ContentValues(2);
                v.put(ZoneButtonLink.COLUMN_BUTTON_ID, button.getId());
                v.put(ZoneButtonLink.COLUMN_ZONE_ID, zoneId);
                database.insert(ZoneButtonLink.TABLE, null, v);
            }
        }

        return zoneId;
    }

    /**
     * Deletes a zone from the database. This will also remove any zone-button links that are
     * stored in the database to maintain database integrity.
     *
     * @param id the id of the zone to delete.
     * @return the number of rows deleted.
     */
    public int deleteZone(long id) {
        int deleted = database.delete(Zone.TABLE, Zone.COLUMN_ID + " = " + id, null);
        deleted += database.delete(ZoneButtonLink.TABLE,
                ZoneButtonLink.COLUMN_ZONE_ID + " = " + id, null);

        return deleted;
    }

    /**
     * Finds the zones, buttons, and commands that are associated with the gateway.
     *
     * @param gateway the gateway.
     * @return a list of the zones, buttons, and ids associated with the gateway.
     */
    public List<Zone> findZones(Gateway gateway) {
        return findZones(gateway.getId());
    }

    /**
     * Finds the zones, buttons, and commands that are associated with the gateway.
     *
     * @param gatewayId the gateway id.
     * @return a list of the zones, buttons, and ids associated with the gateway.
     */
    public List<Zone> findZones(long gatewayId) {
        Cursor cursor = rawQuery(
                "SELECT " +
                        "z._id as zone_id, " +
                        "z.name as zone_name, " +
                        "b._id as button_id, " +
                        "b.name as button_name, " +
                        "c._id as command_id, " +
                        "c.name as command_name, " +
                        "c.gateway_id as gateway_id, " +
                        "c.number as number, " +
                        "c.command_type_id as command_type_id, " +
                        "c.controller_number as controller_number, " +
                        "t.name as command_type_name, " +
                        "t.base_serial_on_code as base_serial_on_code, " +
                        "t.base_serial_off_code as base_serial_off_code, " +
                        "t.activate_controller_selection as activate_controller_selection " +
                        "FROM zone z " +
                        "JOIN zone_button_link zbl " +
                        "ON z._id=zbl.zone_id " +
                        "JOIN button b " +
                        "ON b._id=zbl.button_id " +
                        "JOIN button_command_link bcl " +
                        "ON b._id=bcl.button_id " +
                        "JOIN command c " +
                        "ON c._id=bcl.command_id " +
                        "JOIN command_type t " +
                        "ON t._id=c.command_type_id " +
                        "WHERE c.gateway_id=" + gatewayId + " " +
                        "ORDER BY z._id, b._id asc, c._id asc"
        );

        if (cursor == null) {
            return new ArrayList<>();
        }

        Map<Long, Button> buttons = new HashMap<>();
        Map<Long, Zone> zones = new HashMap<>();

        if (cursor.moveToFirst()) {
            do {
                long zoneId = cursor.getLong(0);
                long buttonId = cursor.getLong(2);

                if (zones.get(zoneId) == null) {
                    Zone zone = new Zone();
                    zone.setId(zoneId);
                    zone.setButtons(new ArrayList<Button>());
                    zone.setName(cursor.getString(1));

                    zones.put(zoneId, zone);
                    buttons = new HashMap<>();
                }

                if (buttons.get(buttonId) == null) {
                    Button button = new Button();
                    button.setId(buttonId);
                    button.setCommands(new ArrayList<Command>());
                    button.setName(cursor.getString(3));

                    buttons.put(buttonId, button);
                }

                Zone zone = zones.get(zoneId);
                Button button = buttons.get(buttonId);

                if (!zone.getButtons().contains(button)) {
                    zone.getButtons().add(button);
                }

                Command command = new Command();
                command.setId(cursor.getLong(4));
                command.setName(cursor.getString(5));
                command.setGatewayId(cursor.getLong(6));
                command.setNumber(cursor.getInt(7));
                command.setCommandTypeId(cursor.getLong(8));

                try {
                    Integer controllerNumber = Integer.parseInt(cursor.getString(9));
                    command.setControllerNumber(controllerNumber);
                } catch (Exception e) {
                    command.setControllerNumber(null);
                }

                CommandType commandType = new CommandType();
                commandType.setId(cursor.getLong(8));
                commandType.setName(cursor.getString(10));
                commandType.setBaseSerialOnCode(cursor.getString(11));
                commandType.setBaseSerialOffCode(cursor.getString(12));
                commandType.setActivateControllerSelection(cursor.getInt(13) == 1);

                command.setCommandType(commandType);

                if (!button.getCommands().contains(command)) {
                    button.getCommands().add(command);
                }
            } while (cursor.moveToNext());
        }

        cursor.close();

        return new ArrayList<>(zones.values());
    }

    /**
     * Gets a string that represents the entire database on this device.
     *
     * @return a string with json arrays holding database rows.
     * @throws JSONException
     */
    public String getDatabaseJson() throws JSONException {
        JSONObject json = new JSONObject();

        json.put(Gateway.TABLE, sqlToJson("select * from gateway"));
        json.put(Command.TABLE, sqlToJson("select * from command"));
        json.put(Button.TABLE, sqlToJson("select * from button"));
        json.put(ButtonCommandLink.TABLE, sqlToJson("select * from button_command_link"));
        json.put(Zone.TABLE, sqlToJson("select * from zone"));
        json.put(ZoneButtonLink.TABLE, sqlToJson("select * from zone_button_link"));

        return json.toString();
    }

    /**
     * Takes a json that was created by getDatabaseJson() and inserts the rows into the database.
     * All old data in those tables will be deleted first.
     *
     * @param json the json object to process.
     */
    public void insertDatabaseJson(JSONObject json) throws JSONException {
        beginTransaction();

        jsonToSql(json.getJSONArray(Gateway.TABLE), Gateway.TABLE);
        jsonToSql(json.getJSONArray(Command.TABLE), Command.TABLE);
        jsonToSql(json.getJSONArray(Button.TABLE), Button.TABLE);
        jsonToSql(json.getJSONArray(ButtonCommandLink.TABLE), ButtonCommandLink.TABLE);
        jsonToSql(json.getJSONArray(Zone.TABLE), Zone.TABLE);
        jsonToSql(json.getJSONArray(ZoneButtonLink.TABLE), ZoneButtonLink.TABLE);

        setTransactionSuccessful();
        endTransaction();
    }

    /**
     * Creates a json array where each item is a row in the database. Each row contains all fields
     * from that table in a comma separated order.
     *
     * @param query the sql string to get the data.
     * @return the json array of data.
     */
    private JSONArray sqlToJson(String query) {
        JSONArray array = new JSONArray();

        Cursor cursor = database.rawQuery(query, null);

        if (cursor != null && cursor.moveToFirst()) {
            do {
                StringBuilder builder = new StringBuilder();
                for (int i = 0; i < cursor.getColumnCount(); i++) {
                    builder.append(cursor.getString(i));
                    if (i != cursor.getColumnCount() - 1) {
                        builder.append(",");
                    }
                }

                array.put(builder.toString());
            } while (cursor.moveToNext());

            cursor.close();
        }

        return array;
    }

    /**
     * Converts a json array of rows to sql insert statements and then puts them into the correct
     * table in the database.
     *
     * @param array the array of rows. Each should be a comma separated string.
     * @param tableName the name of the table to insert data into.
     * @throws JSONException
     */
    private void jsonToSql(JSONArray array, String tableName) throws JSONException {
        // first thing, delete everything currently in the table
        database.execSQL("DELETE FROM " + tableName + ";");

        // insert each row item by item
        for (int i = 0; i < array.length(); i++) {
            String row = array.getString(i);
            String[] items = row.split(",");

            StringBuilder builder = new StringBuilder();
            builder.append("INSERT INTO ");
            builder.append(tableName);
            builder.append(" VALUES (");

            for (int j = 0; j < items.length; j++) {
                if (!items[j].equals("null")) {
                    builder.append("'");
                    builder.append(items[j].replace("'", "''")); // escape apostrophe
                    builder.append("'");
                } else {
                    builder.append("null");
                }

                if (j != items.length - 1) {
                    builder.append(",");
                }
            }

            builder.append(");");
            database.execSQL(builder.toString());
        }
    }

    // You could call this sometime on the system to insert some dummy data for the UI.
    // it will insert the data onto gateway 1, which is changed to the ip address of our
    // home server.
    /*public void insertFakeButtonsAndZones() {
        open();
        execSql("UPDATE gateway SET ip_address = '173.29.143.178' WHERE _id = " + 1);

        insertFakeCommand("Great Room Left", 1);
        insertFakeCommand("Great Room Right", 2);
        insertFakeCommand("Living Room", 3);
        insertFakeCommand("Front Entrance", 4);
        insertFakeCommand("Garage", 5);
        insertFakeCommand("Kitchen Table", 6);
        insertFakeCommand("Dining Room", 7);
        insertFakeCommand("Kitchen", 8);
        insertFakeCommand("Master Bedroom", 13);
        insertFakeCommand("Master Bathroom", 14);
        insertFakeCommand("Kid's Bedroom", 15);

        List<Command> commands = new ArrayList<>();
        commands.addAll(findCommands(1));

        insertNewButton("Great Room", commands.subList(0, 2));
        insertNewButton("Living Room", commands.subList(2, 3));
        insertNewButton("Front Entrance", commands.subList(3, 4));
        insertNewButton("Garage", commands.subList(4, 5));
        insertNewButton("Kitchen Table", commands.subList(5, 6));
        insertNewButton("Kitchen", commands.subList(7, 8));
        insertNewButton("Dining Room", commands.subList(6, 7));
        insertNewButton("Master Bedroom", commands.subList(8, 9));
        insertNewButton("Master Bathroom", commands.subList(9, 10));
        insertNewButton("Kid's Bedroom", commands.subList(10, 11));

        List<Button> buttons = new ArrayList<>();
        buttons.addAll(findButtons(1));

        insertNewZone("Whole House", buttons);
        insertNewZone("Main Floor", buttons.subList(0, 3));
        insertNewZone("Kitchen", buttons.subList(4, 6));
        insertNewZone("Master Suite", buttons.subList(7, 9));
        close();
    }

    private long insertFakeCommand(String name, int number) {
        return insertNewCommand(name, 1, number, 1, null);
    }*/
}
