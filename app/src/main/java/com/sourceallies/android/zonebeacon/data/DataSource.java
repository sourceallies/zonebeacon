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
import com.sourceallies.android.zonebeacon.data.model.Zone;
import com.sourceallies.android.zonebeacon.data.model.ZoneButtonLink;

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

        return database.insert(Gateway.TABLE_GATEWAY, null, values);
    }

    /**
     * Deletes a gateway from the database.
     *
     * @param gatewayId id of the gateway to delete
     * @return the number of items deleted with the statement.
     */
    public int deleteGateway(long gatewayId) {
        // TODO delete all of the zones, buttons and commands associated with the gateway
        return database.delete(Gateway.TABLE_GATEWAY, Gateway.COLUMN_ID + " = " + gatewayId, null);
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

        return database.insert(Command.TABLE_COMMAND, null, values);
    }

    /**
     * Deletes the command from the database. This should be called after deleting buttons
     * associated with this command.
     *
     * @param id the id to delete.
     * @return the number of items deleted.
     */
    public int deleteCommand(long id) {
        return database.delete(Command.TABLE_COMMAND, Command.COLUMN_ID + " = " + id, null);
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

        long buttonId = database.insert(Button.TABLE_BUTTON, null, values);

        if (buttonId != -1) {
            for (Command command : commands) {
                ContentValues v = new ContentValues(2);
                v.put(ButtonCommandLink.COLUMN_COMMAND_ID, command.getId());
                v.put(ButtonCommandLink.COLUMN_BUTTON_ID, buttonId);
                database.insert(ButtonCommandLink.TABLE_BUTTON_COMMAND_LINK, null, v);
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
        int deleted = database.delete(Button.TABLE_BUTTON, Button.COLUMN_ID + " = " + id, null);
        deleted += database.delete(ButtonCommandLink.TABLE_BUTTON_COMMAND_LINK,
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

        long zoneId = database.insert(Zone.TABLE_ZONE, null, values);

        if (zoneId != -1) {
            for (Button button : buttons) {
                ContentValues v = new ContentValues(2);
                v.put(ZoneButtonLink.COLUMN_BUTTON_ID, button.getId());
                v.put(ZoneButtonLink.COLUMN_ZONE_ID, zoneId);
                database.insert(ZoneButtonLink.TABLE_ZONE_BUTTON_LINK, null, v);
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
        int deleted = database.delete(Zone.TABLE_ZONE, Zone.COLUMN_ID + " = " + id, null);
        deleted += database.delete(ZoneButtonLink.TABLE_ZONE_BUTTON_LINK,
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

    // You could call this sometime on the system to insert some dummy data for the UI.
    /*public void insertFakeButtonsAndZones(int gatewayId) {
        insertNewCommand("Light 1", gatewayId, 1, 1, null);
        insertNewCommand("Light 2", gatewayId, 2, 1, null);
        insertNewCommand("Light 3", gatewayId, 3, 1, null);
        insertNewCommand("Light 4", gatewayId, 4, 1, null);
        insertNewCommand("Light 5", gatewayId, 5, 1, null);

        List<Command> commands = new ArrayList<>();
        commands.addAll(findCommands(gatewayId));

        insertNewButton("Living Room", commands.subList(0, 2));
        insertNewButton("Bedroom 1", commands.subList(1, 3));
        insertNewButton("Master Bathroom", commands.subList(3, 4));
        insertNewButton("Stove Light", commands.subList(0, 1));
        insertNewButton("Porch Light", commands.subList(2, 4));

        List<Button> buttons = new ArrayList<>();
        buttons.addAll(findButtons(gatewayId));

        insertNewZone("Whole House", buttons);
        insertNewZone("Basement", buttons.subList(0, 4));
        insertNewZone("Main Floor", buttons.subList(1, 2));
        insertNewZone("Upper Floor", buttons.subList(3, 4));
    }*/
}
