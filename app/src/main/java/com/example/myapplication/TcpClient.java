package com.example.myapplication;

import android.os.AsyncTask;
import android.util.Log;

import java.io.OutputStream;
import java.net.InetAddress;
import java.net.Socket;

public class TcpClient {

    private static TcpClient client;

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
                    Socket socket;
                    InetAddress address = InetAddress.getByName(params[0]);
                    socket = new Socket(address, Integer.parseInt(params[1]));
                    try {
                        output = socket.getOutputStream();
                    } catch (Exception e) {
                        Log.e("TCP", "S: Error", e);
                    } finally {
                        socket.close();
                    }
                } catch (Exception e) {
                    Log.e("TCP", "S: Error", e);
                }
            }
            return null;
        }
    }
}
