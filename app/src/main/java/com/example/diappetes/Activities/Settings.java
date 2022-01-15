package com.example.diappetes.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.diappetes.Global;
import com.example.diappetes.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

public class Settings extends AppCompatActivity {
    // Create fields to associate ui components with
    Button profileSetButton;
    Button doctorSetButton;
    Button deleteAccButton;
    Button logOutButton;
    Button backButton;
    FirebaseFirestore db;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings);

        final Global global = (Global) getApplicationContext();
        db = FirebaseFirestore.getInstance();

        // Associating the variables with ui components
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

                // Prompt a Dialog Box to delete account
                AlertDialog.Builder builder = new AlertDialog.Builder(Settings.this);

                // Ask if user wants to delete account
                builder.setTitle("Delete Account");
                builder.setMessage("Are you sure you want to delete your account?");

                // Dialog Box will disappear if user clicks outside of it
                builder.setCancelable(true);

                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // When the user clicks Continue button
                        // entry is stored in firebase and email is sent to doctor

                        db.collection("Patients").document(global.getNhsNum())
                                .delete()
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Log.d("DB_SETTINGS", "User deleted from Firestore");

                                        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

                                        user.delete()
                                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {
                                                        if (task.isSuccessful()) {
                                                            Log.d("DB_SETTINGS", "Account has been deleted");
                                                            Toast.makeText(Settings.this,"Your account " +
                                                                    "has been deleted",Toast.LENGTH_SHORT).show();
                                                            startActivity(new Intent(getApplicationContext(), Welcome.class));
                                                        }
                                                        else{
                                                            Toast.makeText(Settings.this,"There was an " +
                                                                    "error deleting your account. Please try again.",Toast.LENGTH_SHORT).show();
                                                        }
                                                    }
                                                });
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Log.w("DB_SETTINGS", "Error deleting document", e);
                                    }
                                });

                    }
                });

                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // If user clicks No button dialog box disappears
                        dialog.cancel();
                    }
                });

                AlertDialog alertDialog = builder.create();

                // Show the Alert Dialog box
                alertDialog.show();
            }
        });


    }
}