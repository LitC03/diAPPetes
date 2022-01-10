package com.example.diappetes;

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

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.regex.Pattern;

public class ProfileDetails extends AppCompatActivity {


    Button cancelBtn;
    Button saveBtn;
    EditText firstNameEdit;
    EditText lastNameEdit;
    EditText emailEdit;
    EditText nhsEdit;

//    EditText phoneEdit;
//    EditText typeDiaEdit;
//    EditText typeInsEdit;
//    EditText insAdmEdit;

    FirebaseAuth auth;
    FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile_details);

        final Global global = (Global) getApplicationContext();

        auth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        cancelBtn = (Button) findViewById(R.id.CancelBtn);
        saveBtn = (Button) findViewById(R.id.SaveBtn);

        firstNameEdit = findViewById(R.id.FirstNameEdit);
        lastNameEdit = findViewById(R.id.LastNameEdit);
        emailEdit = findViewById(R.id.EmailEdit);
        nhsEdit = findViewById(R.id.NHSEdit);
//        phoneEdit = findViewById(R.id.PhoneEdit);
//        typeDiaEdit = findViewById(R.id.TypeDiaEdit);
//        typeInsEdit = findViewById(R.id.TypeInsEdit);
//        insAdmEdit = findViewById(R.id.InsAdmEdit);

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
                        nhsEdit.setText(document.getString("NHSNumber")); //Cannot change document name
//                        Get correct names from firebase
//                        phoneEdit.setText(document.getString("phoneNum"));
//                        typeDiaEdit.setText(document.getString("Diabetes type"));
//                        typeInsEdit.setText(document.getString("Insulin type"));
//                        insAdmEdit.setText(document.getString("Insulin administration"));
                    } else {
                        Log.d("PD", "No such document");
                    }
                } else {
                    Log.d("PD", "get failed with ", task.getException());
                }
            }
        });


        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),Settings.class));
            }
        });

        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String firstNameString = firstNameEdit.getText().toString();
                final String lastNameString = lastNameEdit.getText().toString();
                final String emailString = emailEdit.getText().toString();
                final String nhsString = nhsEdit.getText().toString();
//                This can be finalised once the signing up is working correctly
//                final String phoneString = phoneEdit.getText().toString();
//                final String typeDiaString = typeDiaEdit.getText().toString();
//                final String typeInsString = typeInsEdit.getText().toString();
//                final String insAdmString = insAdmEdit.getText().toString();

                if(false) {

                }

                else if(TextUtils.isEmpty(firstNameString)) {
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

                else if(nhsString.length()!=10 || !(isNumeric(nhsString))){ //check the NHS num is a 10 digit sequence
                    Toast.makeText(ProfileDetails.this, "Invalid NHS Number", Toast.LENGTH_LONG).show();
                }


                else{
                    //All fields can be updated once the signing up is finished
                    db.collection("Patients").document(global.getNhsNum())
                            .update(
                                    "fName", firstNameString,
                                    "lName", lastNameString,
                                    "NHSNumber", nhsString,
                                    "email", emailString)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Log.d("PD", "Document updated successfully!");
                                    Toast.makeText(ProfileDetails.this, "Your details have been update", Toast.LENGTH_LONG).show();
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

    private boolean validateEmail(String email){//check that the email is valid
        String regex = "^(?=.{1,64}@)[A-Za-z0-9_-]+(\\.[A-Za-z0-9_-]+)*@" //regex magic for a valid email address
                + "[^-][A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*(\\.[A-Za-z]{2,})$";
        return Pattern.compile(regex).matcher(email).matches();
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