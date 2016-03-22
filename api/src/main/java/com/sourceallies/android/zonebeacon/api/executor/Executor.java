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

package com.sourceallies.android.zonebeacon.api.executor;

import com.sourceallies.android.zonebeacon.api.CommandCallback;
import com.sourceallies.android.zonebeacon.api.interpreter.CentraLiteInterpreter;
import com.sourceallies.android.zonebeacon.api.interpreter.Interpreter;
import com.sourceallies.android.zonebeacon.data.model.Command;
import com.sourceallies.android.zonebeacon.data.model.Gateway;

import java.io.IOException;
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

    /**
     * Static factory method to get the correct executor and interpreter based on the system type.
     *
     * @param gateway The gateway we are going to be using
     * @return Correct executor for the Gateway
     */
    public static Executor createForGateway(Gateway gateway) {
        switch ((int) gateway.getSystemTypeId()) {
            case 1:
                return new SerialExecutor(new CentraLiteInterpreter());
            default:
                return new SerialExecutor(new CentraLiteInterpreter());
        }
    }

    public enum LoadStatus { ON, OFF }

    private Interpreter interpreter;
    private List<OnOffCommand> commands;
    private CommandCallback callback;
    private boolean isRunning = false;

    protected Executor(Interpreter interpreter) {
        this.interpreter = interpreter;
        this.commands = new ArrayList<OnOffCommand>();
    }

    /**
     * Attempts to establish a connection to the gateway.
     *
     * @param gateway the gateway to connect to.
     * @return true if connection can be established, false otherwise.
     */
    public abstract boolean pingGateway(Gateway gateway);

    /**
     * Connects to a gateway.
     *
     * @param gateway the gateway to connect to.
     */
    protected abstract void connect(Gateway gateway);

    /**
     * Disconnects from any gateways that are currently connected.
     */
    protected abstract void disconnect();

    /**
     * Whether or not commands can be combined into a longer string.
     * <p>
     * For example, with Elegance, we can do "^A001^B001" in the same string
     *
     * @return true if commands can be combined, false otherwise
     */
    protected abstract boolean commandsCombinable();

    /**
     * Sends a command directly to the connected gateway.
     *
     * @param command the command to send (processed through the interpreter).
     * @return the response from the gateway. This will need to be processed by the interpreter.
     */
    protected abstract String send(String command) throws IOException;

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
     * @param status the current status of the load (LoadStatus.ON or LoadStatus.OFF)
     */
    public void addCommands(List<Command> commands, LoadStatus status) {
        for (Command command : commands) {
            addCommand(command, status);
        }
    }

    /**
     * Adds a command to the currently established connection. If no connection is established, an
     * exception will be thrown.
     *
     * @param command the command to pass through an interpreter and send through the connection.
     * @param status the current status of the load (LoadStatus.ON or LoadStatus.OFF)
     */
    public void addCommand(Command command, LoadStatus status) {
        if (command == null) {
            throw new RuntimeException("Command cannot be null");
        }

        commands.add(new OnOffCommand(command, status));
    }

    /**
     * Gets a list of the currently added commands.
     *
     * @return the commands that have been added.
     */
    public List<OnOffCommand> getCommands() {
        return commands;
    }

    /**
     * Get the current interpreter for the executor
     *
     * @return the interpreter that has been created.
     */
    protected Interpreter getInterpreter() {
        return interpreter;
    }

    /**
     * Executes a transaction on the currently established connection by sending all commands that
     * have been added.
     */
    public void execute(final Gateway gateway) {
        if (!isRunning) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    isRunning = true;
                    connect(gateway);

                    String commandString = "";
                    while (commands.size() > 0) {
                        OnOffCommand command = commands.get(0);
                        commands.remove(0);

                        if (!commandsCombinable()) {
                            sendCommand(command);
                        } else {
                            commandString += interpreter.getExecutable(command.command, command.status);
                        }
                    }

                    if (!commandString.isEmpty()) {
                        sendCommand(commandString, null);
                    }

                    disconnect();
                    isRunning = false;
                }
            }).start();
        }
    }

    /**
     * Send the command to the gateway
     *
     * @param command command object to send
     */
    protected void sendCommand(OnOffCommand command) {
        sendCommand(interpreter.getExecutable(command.command, command.status), command.command);
    }

    /**
     * Send a string of commands to the gateway
     *
     * @param commandString String of commands to send to the gateway
     * @param command Can be null
     */
    protected void sendCommand(String commandString, Command command) {
        try {
            String response = send(commandString);
            response = interpreter.processResponse(response);

            if (callback != null) {
                callback.onResponse(command, response);
            }
        } catch (Exception e) {

        }
    }

    protected class OnOffCommand {
        public LoadStatus status;
        public Command command;

        public OnOffCommand(Command command, LoadStatus status) {
            this.status = status;
            this.command = command;
        }
    }
}
