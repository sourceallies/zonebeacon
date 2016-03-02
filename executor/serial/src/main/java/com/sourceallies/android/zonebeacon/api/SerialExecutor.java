package com.sourceallies.android.zonebeacon.api;

import com.sourceallies.android.zonebeacon.data.model.Gateway;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import lombok.Getter;

/**
 * Executor that sends commands serially to a control unit. This is required for CentraLite systems
 * and perhaps others as well.
 */
public class SerialExecutor extends Executor {

    private static final long READER_TIMEOUT = 1000;

    private Map<String, SocketConnection> connections;

    public SerialExecutor(Interpreter interpreter) {
        super(interpreter);

        this.connections = new HashMap<>();
    }

    @Override
    public boolean pingGateway(Gateway gateway) {
        // TODO how should we accomplish this?
        return false;
    }

    @Override
    public void connect(Gateway gateway) {
        getOrCreateSocketConnection(gateway);
    }

    @Override
    public void disconnect() {
        Iterator it = connections.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry) it.next();
            disconnect((SocketConnection) pair.getValue());

            it.remove();
        }
    }

    private void disconnect(SocketConnection connection) {
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
     * @param gateway contains address and port number
     * @return string of host.ip:port
     */
    private String buildKey(Gateway gateway) {
        return gateway.getIpAddress() + ":" + gateway.getPortNumber();
    }

    @Override
    protected String send(Gateway gateway, String command) {
        try {
            SocketConnection connection = getOrCreateSocketConnection(gateway);

            PrintWriter w = createPrintWriter(connection);
            w.print(command + "\r\n");
            w.flush();

            BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            StringBuilder sb = new StringBuilder();
            int value;

            setTimeoutOnSocket(connection.getSocket(), gateway);
            while ((value = br.read()) != -1) {
                char c = (char) value;
                sb.append(c);

                if (!br.ready())
                    break;
                else
                    setTimeoutOnSocket(connection.getSocket(), gateway);

            }

            gateway.getHandler().removeCallbacksAndMessages(null);

            return sb.toString();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    protected PrintWriter createPrintWriter(SocketConnection socketConnection) {
        return new PrintWriter(socketConnection.getOutputStream(), true);
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
    private void setTimeoutOnSocket(final Socket socket, final Gateway gateway) {
        // remove any old timeouts
        gateway.getHandler().removeCallbacksAndMessages(null);
        gateway.getHandler().postDelayed(new Runnable() {
            @Override
            public void run() {
                closeSocket(socket);
                getCommandCallback().onResponse(null, "No Response");
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

    /**
     * We want to be able to keep the socket open over multiple commands.
     * So, store them in a HashMap and be able to access them.
     *
     * @param gateway contains the host ip and port.
     * @return connection that was created or found.
     */
    public SocketConnection getOrCreateSocketConnection(Gateway gateway) {
        String key = buildKey(gateway);
        if (connections.containsKey(key)) {
            return connections.get(key);
        } else {
            SocketConnection connection = createSocketConnection(gateway);
            connections.put(key, connection);

            return connection;
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
            } catch (Exception e) {

            }
        }

        public static Socket createSocket(Gateway gateway) {
            try {
                return new Socket(gateway.getIpAddress(), gateway.getPortNumber());
            } catch (Exception e) {
                return null;
            }
        }
    }


}
