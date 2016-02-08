package com.sourceallies.android.zonebeacon.task;

import android.os.AsyncTask;
import android.support.annotation.Nullable;

import org.apache.commons.net.telnet.TelnetClient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.io.Writer;
import java.net.Socket;


public class TcpAsyncTask extends AsyncTask<String, Integer, String> {


    public static void runOnTestServer(String command, @Nullable Callback callback) {
        TcpAsyncTask task = new TcpAsyncTask("192.168.1.150", 11000, callback);
        task.execute(command);
    }

    public interface Callback {
        void onResponse(String responseText);
    }

    private String host;
    private int port;
    private Callback callback;

    public TcpAsyncTask(String host, int port, Callback callback) {
        this(host, port);
        this.callback = callback;
    }

    public TcpAsyncTask(String host, int port) {
        this.host = host;
        this.port = port;
    }

    @Override
    protected String doInBackground(String... commands) {
        try {
            Socket socket = new Socket(host, port);

            InputStream is = socket.getInputStream();
            OutputStream out = socket.getOutputStream();

            PrintWriter w = new PrintWriter(out, true);
            w.print(commands[0] + "\r\n");
            w.flush();

            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            StringBuilder sb = new StringBuilder();
            int value;

            while(br.ready() && (value = br.read()) != -1) {
                char c = (char)value;
                sb.append(c);
            }

            socket.close();

            return sb.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    protected void onPostExecute(String response) {
        if (callback != null) {
            callback.onResponse(response);
        }
    }
}
