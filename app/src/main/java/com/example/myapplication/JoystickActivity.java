package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

public class JoystickActivity extends AppCompatActivity implements JoystickListener {

    TcpClient client = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_joystick);

        Intent intent = getIntent();
        String ip = intent.getStringExtra("ip");
        String port = intent.getStringExtra("port");
        client = new TcpClient();
        client.execute(ip, port);
    }

    public void onJoystickMoved(float x, float y) {
        if (client == null)
            return;

        try {
            String s = x + ", " + y;
            client.writeToServer(s);
        } catch (Exception e) {
            Log.e("TCP", "C: Error", e);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        client.stop();
    }
}
