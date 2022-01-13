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
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class SignUp2 extends AppCompatActivity {
    Button backButton,continueButton;
    EditText diaTypeField,insTypeField,insAdmField,docEmailField,docPhoneField,docNameField;
    ProgressBar progressBar;
    FirebaseFirestore fStore;
    FirebaseAuth fAuth;
    String userID;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup2);

        diaTypeField = findViewById(R.id.diatype);
        insTypeField = findViewById(R.id.instype);
        insAdmField = findViewById(R.id.insadm);
        docEmailField = findViewById(R.id.docemail);
        docPhoneField = findViewById(R.id.docphone);
        docNameField = findViewById(R.id.docname);

        backButton = findViewById(R.id.backBtn);
        continueButton = findViewById(R.id.continueBtn);

        auth = FirebaseAuth.getInstance();

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                startActivity(new Intent(getApplicationContext(),SignUp1.class));
            }
        });

        continueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String emailStr,passStr,nhsNumStr,fNameStr,lNameStr;
                emailStr = passStr = nhsNumStr = fNameStr = lNameStr = null;

                //Get strings from previous activity
                Bundle extras = getIntent().getExtras();
                if (extras != null) {
                    emailStr = extras.getString("emailStr");
                    passStr = extras.getString("passStr");
                    nhsNumStr = extras.getString("nhsNumStr");
                    fNameStr = extras.getString("fNameStr");
                    lNameStr = extras.getString("lNameStr");
                }

                //Get all the data from patient
                final String diabetesType = diaTypeField.getText().toString().trim();
                final String insulinType = insTypeField.getText().toString().trim();
                final String insulinAdm = insAdmField.getText().toString();
                final String docEmail = docEmailField.getText().toString();
                final String docPhone = docPhoneField.getText().toString();
                final String docName = docNameField.getText().toString();
                final Global global = (Global) getApplicationContext();
                final String finalEmailStr = emailStr;
                final String finalPassStr = passStr;
                final String finalFNameStr = fNameStr;
                final String finalLNameStr = lNameStr;
                final String finalNhsNumStr = nhsNumStr;

                //Display an error if email/password fields are empty
                if(TextUtils.isEmpty(diabetesType)) {
                    diaTypeField.setError("Diabetes type is a mandatory field!");
                    return;
                }
                else if(TextUtils.isEmpty(insulinType)) {
                    insTypeField.setError("Insulin type is a mandatory field!");
                    return;
                }


                //Check if another user already uses the same NHS number
                Task<DocumentSnapshot> checkNHSNumTask = FirebaseFirestore.getInstance().collection("Patients")
                        .document(global.getNhsNum()).get();

                checkNHSNumTask.addOnCompleteListener(SignUp2.this, new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            //If there exists an account with the same NHS number, display error message
                            if(checkNHSNumTask.getResult().exists()){ Toast.makeText(SignUp2.this, "NHS Number already used", Toast.LENGTH_SHORT).show();}
                            else{
                                //If no account uses the input NHS number, create new account
                                auth.createUserWithEmailAndPassword(finalEmailStr, finalPassStr).addOnCompleteListener( SignUp2.this,new OnCompleteListener<AuthResult>() {
                                    @Override
                                    public void onComplete(@NonNull Task<AuthResult> task) {
                                        if(task.isSuccessful()){
                                            Toast.makeText(SignUp2.this, "Success", Toast.LENGTH_SHORT).show();
                                            global.setUID(auth.getCurrentUser().getUid());
                                            populateUserData(finalEmailStr, finalFNameStr, finalLNameStr, finalNhsNumStr, global.getUID(),diabetesType,insulinType,insulinAdm,docEmail,docPhone,docName);
                                            startActivity(new Intent(getApplicationContext(),LogMenu.class));
                                        }
                                        else{
                                            Throwable e = task.getException();
                                            Toast.makeText(SignUp2.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                            Log.d("DB_SignUp2", e.getMessage(), e);
                                        }
                                    }
                                });
                            }
                        }
                });
            }
        });
    }

    private void populateUserData(String email, String fName, String lName,
                                  String nhsNum, String UID, String diabetesType, String insulinType,
                                  String insulinAdm, String docEmail, String docPhone, String docName) {

        //Create empty array for alerts
        ArrayList<String> list = new ArrayList<String>();

        Map<String, Object> patient = new HashMap<>(); //Data is submitted to Firestore as hashmaps

        patient.put("fName", fName);
        patient.put("lName", lName);
        patient.put("email", email);
        patient.put("UID", UID);
        patient.put("NHSNumber", nhsNum);
        patient.put("Alerts", list);
        patient.put("DType",diabetesType);
        patient.put("InsType",insulinType);
        patient.put("InsAdm", insulinAdm);
        patient.put("DocName", docName);
        patient.put("DocPhone", docPhone);
        patient.put("DocEmail", docEmail);

        FirebaseFirestore db = FirebaseFirestore.getInstance();

        //Add user to Firestore
        db.collection("Patients").document(nhsNum).set(patient).addOnFailureListener(SignUp2.this, new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(SignUp2.this, "Submitting User Data Failed", Toast.LENGTH_SHORT).show();
                Log.d("DB_SignUp2",e.getMessage());
            }
        });
    }
}
