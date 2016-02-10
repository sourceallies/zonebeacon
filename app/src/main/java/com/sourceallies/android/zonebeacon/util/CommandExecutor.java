package com.sourceallies.android.zonebeacon.util;

import android.app.Activity;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.sourceallies.android.zonebeacon.data.Command;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import lombok.Getter;
import lombok.Setter;

/**
 * Manages the serial communication with the system.
 * <p/>
 * Commands are added and stored in a Queue.
 * Once we get a response on the command, the next command will be executed.
 * <p/>
 * This is a singleton so that it can be injected and we don't have to worry about different
 * modules trying to send commands all at once.
 * <p/>
 * Usage:
 * 1. Just call sendCommand(Command) and it will send an individual command
 * 2. Call addCommand(Command)..., then execute() if you know you are sending more than one
 */
@Singleton
public class CommandExecutor {

    @Setter
    @Inject
    Activity activity;

    private static final String TAG = "CommandExecutor";
    private static final long READER_TIMEOUT = 5000;

    // store the commands
    @Setter
    @Getter
    private List<Command> commands = new ArrayList<>();
    private Map<String, SocketConnection> connections = new HashMap<>();
    private boolean isRunning = false;

    /**
     * adds a command and executes it
     *
     * @param command what we want to send and wherre we are sending it too
     * @return number of commands on the stack
     */
    public int sendCommand(Command command) {
        addCommand(command);
        execute();
        return commands.size();
    }

    /**
     * Add valid commands to the queue.
     *
     * @param command what we want to send and where we are sending it too.
     * @return instance of the command executor so that calls can be chained.
     */
    public CommandExecutor addCommand(Command command) {
        if (command.getCommand() == null) {
            throw new RuntimeException("Commands cannot be null.");
        }

        commands.add(command);

        return this;
    }

    /**
     * Ensure the executor isn't already running, for thread safety.
     * <p/>
     * If it is already running, no reason to do anything, because the commands will be
     * pulled from the queue and sent.
     */
    public void execute() {
        if (!isRunning) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    isRunning = true;

                    while (commands.size() > 0) {
                        Command command = commands.get(0);
                        commands.remove(0);

                        String response = sendCommandOverSocket(command);

                        if (response != null) {
                            provideResponse(command, response);
                        }
                    }

                    shutDownConnections();

                    // if there were more commands added during the shutdown time
                    if (commands.size() > 0) {
                        execute();
                    }

                    isRunning = false;
                }
            }).start();
        }
    }

    /**
     * Open up a TCP socket connection to send commands and receive the response
     *
     * @param command what we want to send and where we are sending it too.
     * @return the system response
     * @throws IOException
     */
    public String sendCommandOverSocket(Command command) {
        try {
            SocketConnection connection = getOrCreateSocketConnection(command);

            PrintWriter w = new PrintWriter(connection.getOutputStream(), true);
            w.print(command.getCommand() + "\r\n");
            w.flush();

            BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            StringBuilder sb = new StringBuilder();
            int value;

            setTimeoutOnSocket(connection.getSocket(), command);
            while ((value = br.read()) != -1) {
                char c = (char) value;
                sb.append(c);

                if (!br.ready())
                    break;
                else
                    setTimeoutOnSocket(connection.getSocket(), command);

            }

            command.getHandler().removeCallbacksAndMessages(null);

            return sb.toString();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * if the socket doesn't provide an output, we do not want it to hang here forever, since br.read()
     * is a blocking method.
     * So, if we haven't gotten a response in 5 seconds, we will close the socket and continue
     * to the next command.
     * <p/>
     * No response only happens if the command does not do anything.
     * EX: the channel 1 light is already on and you try to turn it on again (^A001).
     *
     * @param socket that we are reading from.
     */
    private void setTimeoutOnSocket(final Socket socket, final Command command) {
        // remove any old timeouts
        command.getHandler().removeCallbacksAndMessages(null);
        command.getHandler().postDelayed(new Runnable() {
            @Override
            public void run() {
                closeSocket(socket);
                provideResponse(command, "No Response");
            }
        }, READER_TIMEOUT);
    }

    /**
     * Closes the socket for cleanup
     *
     * @param socket you want to close.
     */
    private void closeSocket(Socket socket) {
        try {
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Run the callback on the UI thread if one is provided
     *
     * @param command  contains a callback
     * @param response text to send to the callback
     */
    private void provideResponse(final Command command, final String response) {
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (command.getCallback() != null)
                    command.getCallback().onResponse(response);
            }
        });
    }

    /**
     * We want to be able to keep the socket open over multiple commands.
     * So, store them in a HashMap and be able to access them.
     *
     * @param command contains the host ip and port
     * @return connection that was created or found.
     */
    private SocketConnection getOrCreateSocketConnection(Command command) {
        String key = buildKey(command);
        if (connections.containsKey(key)) {
            return connections.get(key);
        } else {
            SocketConnection connection = new SocketConnection(command);
            connections.put(key, connection);

            return connection;
        }
    }

    /**
     * Shutdown the connections to save resources after all commands have been sent
     */
    private void shutDownConnections() {
        Iterator it = connections.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry) it.next();
            shutdownConnection((SocketConnection) pair.getValue());

            it.remove();
        }
    }

    /**
     * Shutdown the components of the connection individually. Catch any exceptions.
     *
     * @param connection the connection we want to shut down.
     */
    private void shutdownConnection(SocketConnection connection) {
        try {
            connection.getSocket().close();
        } catch (IOException e) {
        }
        try {
            connection.getInputStream().close();
        } catch (IOException e) {
        }
        try {
            connection.getOutputStream().close();
        } catch (IOException e) {
        }
    }

    /**
     * Returns of string of the address plus port number to communicate on.
     *
     * @param command contains address and port number
     * @return string of host.ip:port
     */
    private String buildKey(Command command) {
        return command.getHost() + ":" + command.getPort();
    }

    /**
     * Holds the components of the socket connection we are keeping active.
     */
    private static class SocketConnection {
        @Getter
        private Socket socket;
        @Getter
        private InputStream inputStream;
        @Getter
        private OutputStream outputStream;

        public SocketConnection(Command command) {
            try {
                socket = new Socket(command.getHost(), command.getPort());
                inputStream = socket.getInputStream();
                outputStream = socket.getOutputStream();
            } catch (IOException e) {

            }
        }
    }
}
