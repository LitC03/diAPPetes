package com.example.diappetes;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreSettings;
import com.google.firebase.firestore.QuerySnapshot;

public class Login extends AppCompatActivity {

    Button loginButton, createAccountButton;
    EditText emailField;
    EditText passwordField;

    FirebaseAuth auth;
    FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);


        loginButton = findViewById(R.id.LoginBtn);
        createAccountButton = findViewById(R.id.RegisterText);
        emailField = findViewById(R.id.EmailField);
        passwordField = findViewById(R.id.PasswordField);

        auth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        final Global global = (Global) getApplicationContext();

        createAccountButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), SignUp1.class));
            }
        });

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){

                String txt_email = emailField.getText().toString();
                String txt_password = passwordField.getText().toString();

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
    }
}
