package com.sourceallies.android.zonebeacon.api;

import com.sourceallies.android.zonebeacon.data.model.Command;
import com.sourceallies.android.zonebeacon.data.model.Gateway;

import java.util.ArrayList;
import java.util.List;

/**
 * Adhere's to the strategy pattern. This interface will be implemented in various different ways.
 * Since each control unit potentially communicates with your device differently, each will need
 * to be able to execute commands differently. That is what this class will handle. Once
 * implemented, the implemented version can be loaded into the app and used to execute commands
 * on different control unit types.
 */
public abstract class Executor {

    private Interpreter interpreter;
    private List<Command> commands;
    private CommandCallback callback;
    private boolean isRunning = false;

    public Executor(Interpreter interpreter) {
        this.interpreter = interpreter;
        this.commands = new ArrayList<Command>();
    }

    /**
     * Attempts to establish a connection to the gateway.
     *
     * @param gateway the gateway to connect to.
     * @return true if connection can be established, false otherwise.
     */
    abstract boolean pingGateway(Gateway gateway);

    /**
     * Connects to a gateway.
     *
     * @param gateway the gateway to connect to.
     */
    abstract void connect(Gateway gateway);

    /**
     * Checks to see if this executor is connected to a gateway.
     *
     * @return true if connected, otherwise false.
     */
    abstract boolean isConnected();

    /**
     * Disconnects from any gateways that are currently connected.
     */
    abstract void disconnect();

    /**
     * Sends a command directly to the connected gateway.
     *
     * @param command the command to send (processed through the interpreter).
     * @return the response from the gateway. This will need to be processed by the interpreter.
     */
    abstract String send(String command);

    /**
     * Sets a callback that should be called when a command has finished sending.
     *
     * @param callback the callback to invoke.
     */
    public void setCommandCallback(CommandCallback callback) {
        this.callback = callback;
    }

    /**
     * Gets the currently set command callback.
     *
     * @return the command callback.
     */
    public CommandCallback getCommandCallback() {
        return callback;
    }

    /**
     * Adds multiple commands at the same time.
     *
     * @param commands the commands to send.
     */
    public void addCommands(List<Command> commands) {
        commands.addAll(commands);
    }

    /**
     * Adds a command to the currently established connection. If no connection is established, an
     * exception will be thrown.
     *
     * @param command the command to pass through an interpreter and send through the connection.
     */
    public void addCommand(Command command) {
        commands.add(command);
    }

    /**
     * Executes a transaction on the currently established connection by sending all commands that
     * have been added.
     */
    public void execute() {
        if (!isRunning && isConnected()) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    isRunning = true;

                    while (commands.size() > 0) {
                        Command command = commands.get(0);
                        commands.remove(0);

                        String response = send(interpreter.getExecutable(command));
                        response = interpreter.processResponse(response);

                        if (response != null && callback != null) {
                            callback.onResponse(command, response);
                        }
                    }

                    isRunning = false;
                }
            }).start();
        }
    }

}
