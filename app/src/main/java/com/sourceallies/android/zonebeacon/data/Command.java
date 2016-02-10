package com.sourceallies.android.zonebeacon.data;

import android.os.Handler;

import lombok.Getter;

/**
 * Command that we are going to send to the system.
 * <p/>
 * Contains a host/IP, port, and command string at a minimum
 * Can provide a command callback as well, to handle the response.
 */
public class Command {

    /**
     * Provides an interface so that we can perform actions on the response string
     */
    public interface CommandCallback {
        void onResponse(String text);
    }

    // A handler is created so that we can set a timeout in case this command fails to return a response.
    @Getter
    private Handler handler;
    @Getter
    private String host;
    @Getter
    private int port;
    @Getter
    private String command;
    @Getter
    private CommandCallback callback;

    public Command(String hostIp, int port) {
        handler = new Handler();

        this.host = hostIp;
        this.port = port;
    }

    public Command setCommand(String command) {
        this.command = command;
        return this;
    }

    public Command setResponseCallback(CommandCallback callback) {
        this.callback = callback;
        return this;
    }
}
