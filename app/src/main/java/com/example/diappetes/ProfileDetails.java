package com.example.diappetes;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

public class ProfileDetails extends AppCompatActivity {

    Button cancelBtn;
    Button saveBtn;
    EditText firstNameEdit;
    EditText lastNameEdit;
    EditText emailEdit;
    EditText phoneEdit;
    EditText nhsEdit;
    EditText typeDiaEdit;
    EditText typeInsEdit;
    EditText insAdmEdit;

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
        phoneEdit = findViewById(R.id.PhoneEdit);
        nhsEdit = findViewById(R.id.NHSEdit);
        typeDiaEdit = findViewById(R.id.TypeDiaEdit);
        typeInsEdit = findViewById(R.id.TypeInsEdit);
        insAdmEdit = findViewById(R.id.InsAdmEdit);

        DocumentReference docRef = db.collection("Patients").document(global.getNhsNum());
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        Log.d("PD", "DocumentSnapshot data: " + document.getData());
                        Log.d("PD", "DocumentSnapshot data: " + document.getString("fName"));
                        firstNameEdit.setText(document.getString("fName"));
                        lastNameEdit.setText(document.getString("lName"));
                        emailEdit.setText(document.getString("email"));
                        nhsEdit.setText(document.getString("NHSNumber")); //Cannot change document name
//                        phoneEdit.setText(document.getString("phoneNume"));
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


            }
        });

    }
}