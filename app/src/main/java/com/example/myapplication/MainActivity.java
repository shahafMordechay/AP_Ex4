package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.io.OutputStream;
import java.io.Serializable;
import java.net.InetAddress;
import java.net.Socket;

public class MainActivity extends AppCompatActivity implements Serializable {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        addListenerOnButton();
    }

    public void addListenerOnButton() {
        final Intent joystickIntent = new Intent(this, JoystickActivity.class);
        Button button = findViewById(R.id.connectBtn);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText ipText = findViewById(R.id.ipEText);
                EditText portText = findViewById(R.id.portEText);
                String ip = ipText.getText().toString();
                String port = portText.getText().toString();
                joystickIntent.putExtra("ip", ip);
                joystickIntent.putExtra("port", port);
                startActivity(joystickIntent);
                finish();
            }
        });
    }
}
