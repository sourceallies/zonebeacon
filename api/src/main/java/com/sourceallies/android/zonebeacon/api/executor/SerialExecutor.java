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

import android.os.Handler;

import com.sourceallies.android.zonebeacon.api.interpreter.Interpreter;
import com.sourceallies.android.zonebeacon.data.model.Gateway;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.List;

import lombok.Getter;

/**
 * Executor that sends commands serially and through a network connection to a control unit.
 * This is required for CentraLite systems and perhaps others as well.
 */
public class SerialExecutor extends Executor {

    private static final long READER_TIMEOUT = 8000;

    private Handler handler;
    private SocketConnection connection;

    protected SerialExecutor(Interpreter interpreter) {
        super(interpreter);
        this.handler = new Handler();
    }

    @Override
    public boolean pingGateway(Gateway gateway) {
        // TODO how should we accomplish this?
        return true;
    }

    @Override
    protected void connect(Gateway gateway) {
        this.connection = createSocketConnection(gateway);
    }

    @Override
    protected void disconnect() {
        try {
            connection.getSocket().close();
        } catch (Exception e) {
        }
        try {
            connection.getInputStream().close();
        } catch (Exception e) {
        }
        try {
            connection.getOutputStream().close();
        } catch (Exception e) {
        }

        connection = null;
    }

    @Override
    protected boolean commandsCombinable() {
        return true;
    }

    @Override
    protected String send(String command) throws IOException {
        PrintWriter w = createPrintWriter(connection);
        w.print(command + "\r\n");
        w.flush();

        BufferedReader br = createBufferedReader(connection);
        StringBuilder sb = new StringBuilder();
        int value;

        setTimeoutOnSocket(connection.getSocket());
        while ((value = br.read()) != -1) {
            char c = (char) value;
            sb.append(c);

            if (!br.ready())
                break;
            else
                setTimeoutOnSocket(connection.getSocket());
        }

        handler.removeCallbacksAndMessages(null);

        return sb.toString();
    }

    protected BufferedReader createBufferedReader(SocketConnection connection) {
        return new BufferedReader(new InputStreamReader(connection.getInputStream()));
    }

    protected PrintWriter createPrintWriter(SocketConnection connection) {
        return new PrintWriter(connection.getOutputStream(), true);
    }

    /**
     * if the socket doesn't provide an output, we do not want it to hang here forever, since br.read()
     * is a blocking method.
     * So, if we haven't gotten a response in 1 second, we will close the socket and continue
     * to the next command.
     * <p/>
     * No response only happens if the command does not do anything.
     * EX: the channel 1 light is already on and you try to turn it on again (^A001).
     *
     * @param socket that we are reading from.
     */
    protected void setTimeoutOnSocket(final Socket socket) {
        // remove any old timeouts
        handler.removeCallbacksAndMessages(null);
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                closeSocket(socket);

                if (getCommandCallback() != null) {
                    getCommandCallback().onResponse(null, "No Response");
                }
            }
        }, READER_TIMEOUT);
    }

    /**
     * Closes the socket for cleanup
     *
     * @param socket you want to close.
     */
    protected void closeSocket(Socket socket) {
        try {
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    protected SocketConnection createSocketConnection(Gateway gateway) {
        return new SocketConnection(gateway);
    }

    /**
     * Holds the components of the socket connection we are keeping active.
     */
    public static class SocketConnection {
        @Getter
        private Socket socket;
        @Getter
        private InputStream inputStream;
        @Getter
        private OutputStream outputStream;

        public SocketConnection(Gateway gateway) {
            this(createSocket(gateway));
        }

        protected SocketConnection(Socket socket) {
            try {
                this.socket = socket;
                this.inputStream = socket.getInputStream();
                this.outputStream = socket.getOutputStream();
            } catch (Throwable e) {
                e.printStackTrace();
            }
        }

        public static Socket createSocket(Gateway gateway) {
            try {
                return new Socket(gateway.getIpAddress(), gateway.getPortNumber());
            } catch (Throwable e) {
                e.printStackTrace();
                return null;
            }
        }
    }


}
