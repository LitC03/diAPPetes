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

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                startActivity(new Intent(getApplicationContext(),SignUp1.class));
            }
        });

        continueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String diabetesType = diaTypeField.getText().toString().trim();
                final String insulinType = insTypeField.getText().toString().trim();
                final String insulinAdm = insAdmField.getText().toString();
                final String docEmail = docEmailField.getText().toString();
                final String docPhone = docPhoneField.getText().toString();
                //final String docName = docNameField.getText().toString();

                //Display an error if email/password fields are empty
                if(TextUtils.isEmpty(diabetesType)) {
                    diaTypeField.setError("Diabetes type is a mandatory field!");
                    return;
                }
                if(TextUtils.isEmpty(insulinType)) {
                    insTypeField.setError("Insulin type is a mandatory field!");
                    return;
                }

                //Add progress bar
                progressBar.setVisibility(View.VISIBLE);


                    //Add user data to firestore NOT DONE YET
                try {
                    userID = fAuth.getCurrentUser().getUid();
                    DocumentReference documentReference = fStore.collection("Patients").document("Pat_"+userID);
                    Map<String,Object> data = new HashMap<>();
                    data.put("Diabetes type",diabetesType);
                    data.put("Insulin type",insulinType);
                    data.put("Insulin administration",insulinAdm);
                    data.put("Alerts", Arrays.asList(docPhone));
                    data.put("Emergency Contact", Arrays.asList(docEmail));

                    documentReference.set(data).addOnSuccessListener(new OnSuccessListener<Void>() {
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
                    startActivity(new Intent(getApplicationContext(), MainActivity.class));

                    //Hide progess bar if user has been added to batabse
                    progressBar.setVisibility(View.INVISIBLE);
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });
    }
}
