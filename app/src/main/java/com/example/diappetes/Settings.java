package com.example.diappetes;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;

public class Settings extends AppCompatActivity {

    Button profileSetButton;
    Button doctorSetButton;
    Button deleteAccButton;
    Button logOutButton;
    Button backButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings);

        profileSetButton = (Button) findViewById(R.id.ProfileSetBtn);
        doctorSetButton = (Button) findViewById(R.id.DoctorSetBtn);
        deleteAccButton = (Button) findViewById(R.id.DeleteAccBtn);
        logOutButton = (Button) findViewById(R.id.LogOutBtn);
        backButton = (Button) findViewById(R.id.BacktoMainBtn);

        // Button that opens Profile Details page
        profileSetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),ProfileDetails.class));
            }
        });

        // Button that opens Doctor's Details page
        doctorSetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),DoctorDetails.class));
            }
        });

        // Button to log out of account
        logOutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(getApplicationContext(), Welcome.class));
            }
        });

        // Back button to go back to main menu
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),MainActivity.class));
            }
        });


        // Button to delete account
        deleteAccButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TO BE WRITTEN
                // maybe add an "are you sure you want to delete your account?" dialogue or something
            }
        });


    }
}