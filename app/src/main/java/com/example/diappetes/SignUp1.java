package com.example.diappetes;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
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

import java.util.regex.Pattern;

public class SignUp1 extends AppCompatActivity {
    Button backButton,continueButton;
    private EditText firstName;
    private EditText lastName;
    private EditText password;
    private EditText email;
    private EditText phone;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup1);


        backButton = findViewById(R.id.backBtn);
        continueButton = findViewById(R.id.continueBtn);

        firstName = findViewById(R.id.firstNameField);
        lastName = findViewById(R.id.lastNameField);
        password = findViewById(R.id.passwordField);
        email = findViewById(R.id.emailField);
        phone = findViewById(R.id.phoneField);

        auth = FirebaseAuth.getInstance();

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                startActivity(new Intent(getApplicationContext(),Welcome.class));
            }
        });

        continueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){

                String txt_email = email.getText().toString();
                String txt_password = password.getText().toString();
                
                if(TextUtils.isEmpty(txt_email) || TextUtils.isEmpty(txt_password)){
                    Toast.makeText( SignUp1.this, "Please enter a username and password", Toast.LENGTH_SHORT).show();
                }
                else if(!validateEmail(txt_email)){
                    Toast.makeText(SignUp1.this, "Invalid Email Adress", Toast.LENGTH_LONG).show();
                }
                else if(!validatePassword(txt_password)) { //add requirements for special characters here
                    Toast.makeText(SignUp1.this, "Password must be longer than 8 characters and contain at least 1 special character", Toast.LENGTH_SHORT).show();
                }
                else{
                    auth.createUserWithEmailAndPassword(txt_email, txt_password).addOnCompleteListener( SignUp1.this,new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()){
                                Toast.makeText(SignUp1.this, "Success", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(getApplicationContext(),SignUp2.class));
                            }
                            else{
                                Throwable e = task.getException();
                                Toast.makeText(SignUp1.this, "FAIL", Toast.LENGTH_SHORT).show();
                                Log.d(e.getClass().getName(), e.getMessage(), e);
                            }
                        }
                    });
                }
            }
        });
    }

    private static boolean validateEmail(String email){//check that the email is valid
        String regex = "^(?=.{1,64}@)[A-Za-z0-9_-]+(\\.[A-Za-z0-9_-]+)*@" //regex magic for a valid email address
                + "[^-][A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*(\\.[A-Za-z]{2,})$";
        return Pattern.compile(regex).matcher(email).matches();
    }

    private static boolean validatePassword(String password){
        String regex = "[a-zA-Z0-9 ]*"; //more regex magic, this time you check it doesn't match this regex (to prove it contains a character outside the alphanumeric set)
        return (password.length()>7)&&!(Pattern.compile(regex).matcher(password).matches());
    }
}