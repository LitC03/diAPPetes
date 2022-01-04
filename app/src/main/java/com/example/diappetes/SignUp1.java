package com.example.diappetes;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class SignUp1 extends AppCompatActivity {
    Button backButton,continueButton;
    EditText FirstNameField,LastNameField,EmailField,PasswordField,PhoneField;
    ProgressBar progressBar;
    FirebaseFirestore fStore;
    FirebaseAuth fAuth;
    String userID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup1);

        //"handles" of fields/buttons in Register page
        FirstNameField = findViewById(R.id.firstNameField);
        LastNameField = findViewById(R.id.lastNameField);
        EmailField = findViewById(R.id.emailField);
        PasswordField = findViewById(R.id.passwordField);
        PhoneField = findViewById(R.id.phoneField);

        backButton = findViewById(R.id.backBtn);
        continueButton = findViewById(R.id.continueBtn);

        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();
        progressBar = findViewById(R.id.progressBar);


        //Go back to welcome page if "back" button is pressed
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                startActivity(new Intent(getApplicationContext(),Welcome.class));
            }
        });



        continueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String email = EmailField.getText().toString().trim();
                final String password = PasswordField.getText().toString().trim();
                final String phone = PhoneField.getText().toString();
                final String FName = FirstNameField.getText().toString();
                final String LName = LastNameField.getText().toString();

                //Display an error if email/password fields are empty
                if(TextUtils.isEmpty(email)) {
                    EmailField.setError("Email is a mandatory field!");
                    return;
                }
                if(TextUtils.isEmpty(password)) {
                    PasswordField.setError("Password is a mandatory field!");
                    return;
                }

                //Add progress bar
                progressBar.setVisibility(View.VISIBLE);

                // Register user in firebase (Authentification)
                fAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        //If the task is successful, add user to firestore
                        if(task.isSuccessful()){
                            //Show success message to user
                            Toast.makeText(SignUp1.this, "Your account has been created!", Toast.LENGTH_SHORT).show();

                            //Add user data to firestore
                            userID = fAuth.getCurrentUser().getUid();
                            DocumentReference documentReference = fStore.collection("Patients").document("Pat_"+userID);
                            Map<String,Object> user = new HashMap<>();
                            user.put("FName",FName);
                            user.put("LName",LName);
                            user.put("email",email);
                            user.put("phone",phone);
                            user.put("password", password);

                            documentReference.set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Log.d("TAG", "onSuccess: user Profile is created for Pat_"+ userID);
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.d("TAG", "onFailure: "+e.toString());
                                }
                            });

                            //Open new page
                            startActivity(new Intent(getApplicationContext(), SignUp2.class));

                            //Hide progess bar if user has been added to batabse
                            progressBar.setVisibility(View.INVISIBLE);
                        }else {
                            //Display error message if something went wrong
                            Toast.makeText(SignUp1.this, ""+task.getException().getMessage(), Toast.LENGTH_SHORT).show();

                            //Hide progress bar
                            progressBar.setVisibility(View.GONE);
                            progressBar.setVisibility(View.INVISIBLE);
                        }
                    }
                });
            }
        });
    }
}
