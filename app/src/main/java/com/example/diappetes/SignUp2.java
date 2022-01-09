package com.example.diappetes;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class SignUp2 extends AppCompatActivity {
    Button backButton, continueButton;
    EditText DiaTypeField, InsTypeField, InsAdmField, DocEmailField,DocPhoneField,DocNameField;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup2);

        backButton = findViewById(R.id.backBtn);
        continueButton = findViewById(R.id.continueBtn);

        DiaTypeField = findViewById(R.id.DiaTypeField);
        InsTypeField = findViewById(R.id.InsTypeField);
        InsAdmField = findViewById(R.id.InsAdmField);
        DocEmailField = findViewById(R.id.DocEmailField);
        DocPhoneField = findViewById(R.id.DocPhoneField);
        DocNameField = findViewById(R.id.DocNameField);

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), SignUp1.class));
            }
        });

        continueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Map<String, Object> data = new HashMap<>();

                data.put("DiabetesType",DiaTypeField.getText().toString());
                data.put("InsulinType",InsTypeField.getText().toString());
                data.put("InsulinAdministration", InsAdmField.getText().toString());
                data.put("DoctorEmail", DocEmailField.getText().toString());
                data.put("DoctorPhone",DocPhoneField.getText().toString());
                data.put("DoctorName", DocNameField.getText().toString());

                populateUserData(data);

                startActivity(new Intent(getApplicationContext(), MainActivity.class));
            }
        });


    }

    private void populateUserData(Map<String, Object> data) {

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        Global global = (Global) getApplicationContext();
        db.collection("Patients").document(global.getNhsNum()).update(data)
                .addOnFailureListener(SignUp2.this, new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(SignUp2.this, "Submitting User Data Failed", Toast.LENGTH_SHORT).show();
                }
            });
    }
}