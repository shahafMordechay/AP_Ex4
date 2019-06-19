package com.example.myapplication;

import android.os.AsyncTask;
import android.util.Log;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class TcpClient extends AsyncTask<String, String, Void> {
    private BlockingQueue<String> joystickData = new LinkedBlockingQueue<>();
    private boolean stop = false;

    public void writeToServer(String s) {
        try {
            this.joystickData.put(s);
        } catch (Exception e) {
            Log.e("TCP", "S: Error", e);
        }
    }

    public void stop() {
        try {
            stop = true;
            joystickData.put("stop");
        } catch (Exception e) {
            Log.e("TCP", "S: Error", e);
        }
    }

    @Override
    protected Void doInBackground(String... params) {
        try {
            Socket socket = new Socket(params[0], Integer.parseInt(params[1]));
            DataOutputStream output = new DataOutputStream(socket.getOutputStream());
            try {
                while (!stop) {
                    String data = joystickData.take();
                    if (!data.equals("stop")) {
                        output.write((data + "\r\n").getBytes());
                        output.flush();
                    }
                }
            } catch (IOException e) {
                Log.e("TCP", "S: Error", e);
            } finally {
                socket.close();
            }
        } catch (Exception e) {
            Log.e("TCP", "C: Error", e);
        }
        return null;
    }
}
