package com.example.diappetes.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.diappetes.Global;
import com.example.diappetes.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.regex.Pattern;

public class ProfileDetails extends AppCompatActivity {
    // Create fields to associate ui components with
    Button cancelBtn, saveBtn;
    EditText firstNameEdit, lastNameEdit, emailEdit, typeDiaEdit, typeInsEdit, insAdmEdit;
    FirebaseAuth auth;
    FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile_details);

        final Global global = (Global) getApplicationContext();

        auth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        // Associating the variables with ui components
        cancelBtn = (Button) findViewById(R.id.CancelBtn);
        saveBtn = (Button) findViewById(R.id.SaveBtn);
        firstNameEdit = findViewById(R.id.FirstNameEdit);
        lastNameEdit = findViewById(R.id.LastNameEdit);
        emailEdit = findViewById(R.id.EmailEdit);
        typeDiaEdit = findViewById(R.id.TypeDiaEdit);
        typeInsEdit = findViewById(R.id.TypeInsEdit);
        insAdmEdit = findViewById(R.id.InsAdmEdit);

        // Display current details from the database
        DocumentReference docRef = db.collection("Patients").document(global.getNhsNum());
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        Log.d("PD", "DocumentSnapshot data: " + document.getData());
                        firstNameEdit.setText(document.getString("fName"));
                        lastNameEdit.setText(document.getString("lName"));
                        emailEdit.setText(document.getString("email"));
                        typeDiaEdit.setText(document.getString("DType"));
                        typeInsEdit.setText(document.getString("InsType"));
                        insAdmEdit.setText(document.getString("InsAdm"));
                    } else {
                        Log.d("PD", "No such document");
                    }
                } else {
                    Log.d("PD", "get failed with ", task.getException());
                }
            }
        });

        // Cancel button to go back to previous menu
        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),Settings.class));
            }
        });

        // Save button to check and update data to be sent to firebase database
        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String firstNameString = firstNameEdit.getText().toString();
                final String lastNameString = lastNameEdit.getText().toString();
                final String emailString = emailEdit.getText().toString();
                final String typeDiaString = typeDiaEdit.getText().toString();
                final String typeInsString = typeInsEdit.getText().toString();
                final String insAdmString = insAdmEdit.getText().toString();

                // Mandatory field checks
                if(TextUtils.isEmpty(firstNameString)) {
                    Toast.makeText(ProfileDetails.this, "Please enter your first name", Toast.LENGTH_LONG).show();
                }

                else if(TextUtils.isEmpty(lastNameString)) {
                    Toast.makeText(ProfileDetails.this, "Please enter your last name", Toast.LENGTH_LONG).show();
                }

                else if(TextUtils.isEmpty(emailString)) {
                    Toast.makeText(ProfileDetails.this, "Please enter your email", Toast.LENGTH_LONG).show();
                }

                else if(!validateEmail(emailString)) {
                    Toast.makeText(ProfileDetails.this, "Please enter a correct email", Toast.LENGTH_LONG).show();
                }

                else if(TextUtils.isEmpty(emailString)) {
                    Toast.makeText(ProfileDetails.this, "Please enter your email", Toast.LENGTH_LONG).show();
                }


                else{
                    // All fields can be updated once the signing up is finished
                    db.collection("Patients").document(global.getNhsNum())
                            .update(
                                    "fName", firstNameString,
                                    "lName", lastNameString,
                                    "email", emailString,
                                    "DType", typeDiaString,
                                    "InsType", typeInsString,
                                    "InsAdm", insAdmString)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Log.d("PD", "Document updated successfully!");
                                    Toast.makeText(ProfileDetails.this, "Your details have been updated", Toast.LENGTH_LONG).show();
                                    startActivity(new Intent(getApplicationContext(), Settings.class));
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(ProfileDetails.this, "Your could not be updated, please try again later", Toast.LENGTH_LONG).show();
                                    Log.w("PD", "Error updating document", e);
                                }
                            });

                }

            }
        });

    }
    // Check that the email is valid
    private boolean validateEmail(String email){
        String regex = "^(?=.{1,64}@)[A-Za-z0-9_-]+(\\.[A-Za-z0-9_-]+)*@" //regex magic for a valid email address
                + "[^-][A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*(\\.[A-Za-z]{2,})$";
        return Pattern.compile(regex).matcher(email).matches();
    }

    private static boolean isNumeric(String str){
        try {
            Double.parseDouble(str);
            return true; // If it can be converted to an int, it is numeric
        } catch(NumberFormatException e){
            return false; // If it can't be converted to an int, it is not numeric
        }
    }
}