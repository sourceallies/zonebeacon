package com.sourceallies.android.zonebeacon.api;

import com.sourceallies.android.zonebeacon.data.model.Gateway;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;

import lombok.Getter;

/**
 * Executor that sends commands serially and through a network connection to a control unit.
 * This is required for CentraLite systems and perhaps others as well.
 */
public class SerialExecutor extends Executor {

    private static final long READER_TIMEOUT = 1000;

    private Gateway gateway;
    private SocketConnection connection;

    public SerialExecutor(Interpreter interpreter) {
        super(interpreter);
    }

    @Override
    public boolean pingGateway(Gateway gateway) {
        // TODO how should we accomplish this?
        return false;
    }

    @Override
    public void connect(Gateway gateway) {
        this.gateway = gateway;
        this.connection = createSocketConnection(gateway);
    }

    @Override
    public void disconnect() {
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

        connection = null;
    }

    @Override
    protected String send(String command) {
        try {
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
