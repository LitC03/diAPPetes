package com.example.diappetes;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class SignUp1 extends AppCompatActivity {
    Button backbutton,continuebutton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup1);


        backbutton = findViewById(R.id.backBtn);
        continuebutton = findViewById(R.id.continueBtn);

        backbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                startActivity(new Intent(getApplicationContext(),Welcome.class));
            }
        });

        continuebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                startActivity(new Intent(getApplicationContext(),SignUp2.class));
            }
        });
    }
}
