package com.example.diappetes;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;

public class SignUp2 extends AppCompatActivity {
    Button backButton,continueButton;
    Spinner typeDiaSpinner,insAdmSpinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup2);

        backButton = (Button) findViewById(R.id.backBtn);
        continueButton = (Button) findViewById(R.id.continueBtn);
        typeDiaSpinner = (Spinner) findViewById(R.id.TypeDiaSpinner);
        insAdmSpinner = (Spinner) findViewById(R.id.InsAdmSpinner);

        ArrayAdapter<String> typeDiaAdapter = new ArrayAdapter<String>(SignUp2.this,
                android.R.layout.simple_list_item_1,
                getResources().getStringArray(R.array.DiaTypes));
        typeDiaAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        typeDiaSpinner.setAdapter(typeDiaAdapter);

        ArrayAdapter<String> insAdmAdapter = new ArrayAdapter<String>(SignUp2.this,
                android.R.layout.simple_list_item_1,
                getResources().getStringArray(R.array.InsulinAdm));
        insAdmAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        insAdmSpinner.setAdapter(insAdmAdapter);

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                startActivity(new Intent(getApplicationContext(),SignUp1.class));
            }
        });

        continueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                startActivity(new Intent(getApplicationContext(),MainActivity.class));
            }
        });


    }


}
