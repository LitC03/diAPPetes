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

public class Login extends AppCompatActivity {

    Button loginButton;
    EditText emailField;
    EditText passwordField;

    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);


        loginButton = findViewById(R.id.LoginBtn);
        emailField = findViewById(R.id.emailField);
        passwordField = findViewById(R.id.passwordField);

        auth = FirebaseAuth.getInstance();

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                String txt_email = emailField.getText().toString();
                String txt_password = emailField.getText().toString();

                auth.signInWithEmailAndPassword(txt_email, txt_password).addOnCompleteListener( Login.this,new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(Login.this, "Success", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(getApplicationContext(),MainActivity.class));
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
