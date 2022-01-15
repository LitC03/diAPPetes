package com.example.diappetes.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.diappetes.R;

public class MainActivity extends AppCompatActivity {

    Button logBookBtn;
    Button settingsBtn;
    Button historyBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Button that opens the Log Menu
        logBookBtn = (Button) findViewById(R.id.LogBookBtn);
        logBookBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                startActivity(new Intent(getApplicationContext(),LogMenu.class));
            }
        });

        // Button that opens Settings
        settingsBtn = (Button) findViewById(R.id.SettingsBtn);
        settingsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),Settings.class));
            }
        });

        // Button that opens History
        historyBtn = (Button) findViewById(R.id.HistoryBtn);
        historyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),History.class));
            }
        });

    }


}