package com.example.myapplication;

import android.os.AsyncTask;
import android.util.Log;

import java.io.OutputStream;
import java.net.Socket;

public class TcpClient {

    private static TcpClient client;

    private Socket socket;
    private OutputStream output;

    private TcpClient(String ip, String port) {
        ConnectToServer server = new ConnectToServer();
        server.execute(ip, port);
    }

    public static TcpClient getInstance(String ip, String port) {
        if (client == null) {
            synchronized (TcpClient.class) {
                if (client == null) {
                    client = new TcpClient(ip, port);
                }
            }
        }

        return client;
    }

    public void writeToServer(String s) {
        try {
            output.write(s.getBytes());
            output.flush();
        } catch (Exception e) {
            Log.e("TCP", "S: Error", e);
        }
    }

    private class ConnectToServer extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {
            if (output == null) {
                try {
                    socket = new Socket(params[0], Integer.parseInt(params[1]));
                    output = socket.getOutputStream();
                } catch (Exception e) {
                    Log.e("TCP", "S: Error", e);
                }
            }
            return null;
        }
    }
}
