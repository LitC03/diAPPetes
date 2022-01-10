package com.example.diappetes;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

public class SignUp1 extends AppCompatActivity {
    Button backButton,continueButton;
    private EditText firstName;
    private EditText lastName;
    private EditText password;
    private EditText email;
    private EditText nhsNum;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup1);


        backButton = findViewById(R.id.backBtn);
        continueButton = findViewById(R.id.continueBtn);

        firstName = findViewById(R.id.FirstNameField);
        lastName = findViewById(R.id.LastNameField);
        password = findViewById(R.id.PasswordField);
        email = findViewById(R.id.EmailField);
        nhsNum = findViewById(R.id.nhsNumField);

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

                final String txt_email = email.getText().toString();
                final String txt_password = password.getText().toString();
                final String txt_nhsNum = nhsNum.getText().toString();
                final String txt_fName = firstName.getText().toString();
                final String txt_lName = lastName.getText().toString();

                final Global global = (Global) getApplicationContext();
                global.setNhsNum(txt_nhsNum);

                final Task<DocumentSnapshot> checkNHSNumTask = FirebaseFirestore.getInstance().collection("Patients").document(txt_nhsNum).get();
                //At some point this block should be converted to a switch
                if(TextUtils.isEmpty(txt_email) || TextUtils.isEmpty(txt_password)){
                    Toast.makeText( SignUp1.this, "Please enter a username and password", Toast.LENGTH_SHORT).show();
                }
                else if(!validateEmail(txt_email)){ //check the email is valid
                    Toast.makeText(SignUp1.this, "Invalid Email Address", Toast.LENGTH_LONG).show();
                }
                else if(!validatePassword(txt_password)) { //check if a password meets the requirements
                    Toast.makeText(SignUp1.this, "Password must be longer than 8 characters and contain at least 1 special character", Toast.LENGTH_SHORT).show();
                }
                else if(txt_nhsNum.length()!=10 || !(isNumeric(txt_nhsNum))){ //check the NHS num is a 10 digit sequence
                    Toast.makeText(SignUp1.this, "Invalid NHS Number", Toast.LENGTH_SHORT).show();
                }
                else{
                    checkNHSNumTask.addOnCompleteListener(SignUp1.this, new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if(checkNHSNumTask.getResult().exists()){ Toast.makeText(SignUp1.this, "NHS Number already used", Toast.LENGTH_SHORT).show();}
                        else{
                        auth.createUserWithEmailAndPassword(txt_email, txt_password).addOnCompleteListener( SignUp1.this,new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if(task.isSuccessful()){
                                    Toast.makeText(SignUp1.this, "Success", Toast.LENGTH_SHORT).show();
                                    global.setUID(auth.getCurrentUser().getUid());
                                    populateUserData(txt_email, txt_fName, txt_lName, txt_nhsNum, global.getUID());
                                    startActivity(new Intent(getApplicationContext(),SignUp2.class));
                                }
                                else{
                                    Throwable e = task.getException();
                                    Toast.makeText(SignUp1.this, "FAIL", Toast.LENGTH_SHORT).show();
                                    Log.d(e.getClass().getName(), e.getMessage(), e);
                                }
                                }
                            });}
                            }
                    });
                }
            }
        });
    }

    private boolean validateEmail(String email){//check that the email is valid
        String regex = "^(?=.{1,64}@)[A-Za-z0-9_-]+(\\.[A-Za-z0-9_-]+)*@" //regex magic for a valid email address
                + "[^-][A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*(\\.[A-Za-z]{2,})$";
        return Pattern.compile(regex).matcher(email).matches();
    }

    private boolean validatePassword(String password){
        String regex = "[a-zA-Z0-9 ]*"; //more regex magic, this time you check it doesn't match this regex (to prove it contains a character outside the alphanumeric set)
        return (password.length()>7)&&!(Pattern.compile(regex).matcher(password).matches());
    }

    private void populateUserData(String email, String fName, String lName, String nhsNum, String UID){
        String AlertsArray[] = new String[0];
        Map<String, Object> patient = new HashMap<>(); //data submitted to Firestore as hashmaps

        patient.put("fName", fName);
        patient.put("lName", lName);
        patient.put("email", email);
        patient.put("UID", UID);
        patient.put("nhsnumber", nhsNum);
        patient.put("Alerts", AlertsArray);

        FirebaseFirestore db = FirebaseFirestore.getInstance();


        db.collection("Patients").document(nhsNum).set(patient).addOnFailureListener(SignUp1.this, new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(SignUp1.this, "Submitting User Data Failed", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private static boolean isNumeric(String str){
        try {
            Double.parseDouble(str);
            return true; //if it can be converted to an int, it is numeric
        } catch(NumberFormatException e){
            return false; //if it can't be converted to an int, it is not numeric
        }
    }
}