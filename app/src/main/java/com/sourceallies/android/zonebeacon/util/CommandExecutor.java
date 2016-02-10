package com.sourceallies.android.zonebeacon.util;

import android.app.Activity;
import android.util.Log;

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
import java.util.LinkedList;
import java.util.Queue;

/**
 * Manages the serial communication with the system.
 *
 * Commands are added and stored in a Queue.
 * Once we get a response on the command, the next command will be executed.
 *
 * This is a singleton so that it can be injected and we don't have to worry about different
 * modules trying to send commands all at once.
 */
@Singleton
public class CommandExecutor {

    @Inject Activity activity;

    private static final String TAG = "CommandExecutor";

    // store the commands in a queue
    private Queue<Command> commands = new LinkedList<>();
    private boolean isRunning = false;

    /**
     * called to send a command to the system
     *
     * @param command what we want to send and wherre we are sending it too
     */
    public synchronized void sendCommand(Command command) {
        addCommand(command);
        execute();
    }

    /**
     * Adds valid commands to the queue
     *
     * @param command what we want to send and where we are sending it too.
     */
    private synchronized void addCommand(Command command) {
        if (command.getCommand() == null) {
            throw new RuntimeException("Commands cannot be null.");
        }

        commands.add(command);
    }

    /**
     * Ensure the executer isn't already running, for thread safety.
     *
     * If it is already running, no reason to do anything, because the commands will be
     * pulled from the queue and sent.
     */
    private synchronized void execute() {
        if (!isRunning) {
            executeCommands();
        }
    }

    /**
     * Execute the commands on a background thread.
     */
    private void executeCommands() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                isRunning = true;

                while (!commands.isEmpty()) {
                    Command command = commands.remove();
                    String response = null;

                    try {
                        response = sendCommandOverSocket(command);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    if (response != null) {
                        provideResponse(command, response);
                    }
                }

                isRunning = false;
            }
        }).start();
    }

    /**
     * Open up a TCP socket connection to send commands and receive the response
     *
     * @param command what we want to send and where we are sending it too.
     * @return the system response
     * @throws IOException
     */
    private String sendCommandOverSocket(Command command) throws IOException {
        Socket socket = new Socket(command.getHost(), command.getPort());

        InputStream is = socket.getInputStream();
        OutputStream out = socket.getOutputStream();

        PrintWriter w = new PrintWriter(out, true);
        w.print(command.getCommand() + "\r\n");
        w.flush();

        BufferedReader br = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();
        int value;

        Log.v(TAG, "reader ready: " + br.ready());

        while((value = br.read()) != -1) {
            char c = (char)value;
            sb.append(c);

            if (!br.ready()) break;
        }

        socket.close();

        return sb.toString();
    }

    /**
     * Run the callback on the UI thread if one is provided
     *
     * @param command contains a callback
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
}
