package com.example.diappetes.Activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.diappetes.Global;
import com.example.diappetes.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

public class Login extends AppCompatActivity {
    // Create fields to associate ui components with
    Button loginButton, createButton,  forgotButton;
    EditText emailField, passwordField;
    FirebaseAuth auth;
    FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        // Associating the variables with ui components
        loginButton = findViewById(R.id.LoginBtn);
        createButton = findViewById(R.id.CreateAccBtn);
        forgotButton = findViewById(R.id.ForgotPWBtn);
        emailField = findViewById(R.id.Email);
        passwordField = findViewById(R.id.password);

        auth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        final Global global = (Global) getApplicationContext();

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){

                String txt_email = emailField.getText().toString();
                String txt_password = passwordField.getText().toString();

                // Check log in details
                auth.signInWithEmailAndPassword(txt_email, txt_password).addOnCompleteListener( Login.this,new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(Login.this, "Success", Toast.LENGTH_SHORT).show();
                            Log.d("DBACCESS", "Auth Successful");
                            global.setUID(auth.getCurrentUser().getUid());

                            db.collection("Patients").whereEqualTo("UID", auth.getCurrentUser().getUid())
                                    .get()
                                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                        @Override
                                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                            if (task.getResult().getDocuments().isEmpty()) {
                                                Log.d("DBACCESS", "No documents found");
                                            } else {
                                                Log.d("DBACCESS", task.getResult().getDocuments().get(0).getId());
                                                global.setNhsNum(task.getResult().getDocuments().get(0).getId());
                                                // Successful login leads user to Main Menu
                                                startActivity(new Intent(getApplicationContext(), MainActivity.class));
                                            }
                                        }
                                    });

                        }
                        else{
                            Throwable e = task.getException();
                            Toast.makeText(Login.this, "FAIL", Toast.LENGTH_SHORT).show();
                            assert e != null;
                            Log.d(e.getClass().getName(), e.getMessage(), e);
                        }
                    }
                });


            }
        });

        // Button to go to first sign up page
        createButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),SignUp1.class));
            }
        });

        // Button to go to reset password
        forgotButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Alert dialog appears asking user for email address
                // User can input address or cancel process
                final EditText resetMail = new EditText(v.getContext());
                final AlertDialog.Builder passwordResetDialog = new AlertDialog.Builder(v.getContext());
                passwordResetDialog.setTitle("Reset password");
                passwordResetDialog.setMessage("Enter your email");
                passwordResetDialog.setView(resetMail);

                // If the user has entered email address
                passwordResetDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Get email address
                        String email = resetMail.getText().toString();
                        // Send email with reset link
                        auth.sendPasswordResetEmail(email).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                Toast.makeText(Login.this,
                                        "A link to reset your password has been sent to your email address",
                                        Toast.LENGTH_SHORT).show();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(Login.this,"Error"+ e.getMessage(),
                                        Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                });

                // If the user cancels
                passwordResetDialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });
                passwordResetDialog.create().show();
            }
        });

    }
}