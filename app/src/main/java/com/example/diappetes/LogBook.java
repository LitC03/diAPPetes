package com.example.diappetes;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class LogBook extends AppCompatActivity {
    Button bloodbutton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.logbook);

        bloodbutton = findViewById(R.id.LogBookBtn);

        bloodbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                startActivity(new Intent(getApplicationContext(),BloodSugar.class));
            }
        });
    }
}
