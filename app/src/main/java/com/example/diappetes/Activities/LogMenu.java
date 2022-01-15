package com.example.diappetes.Activities;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.diappetes.R;

public class LogMenu extends AppCompatActivity {

    Button bloodSugarLogBtn;
    Button foodLogBtn;
    Button medicationLogBtn;
    Button exerciseLogBtn;
    Button extraNotesLogBtn;
    Button questionnaireBtn;
    Button backButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.log_menu);

        bloodSugarLogBtn = (Button) findViewById(R.id.BloodSugarLogBtn);
        foodLogBtn = (Button) findViewById(R.id.FoodLogBtn);
        medicationLogBtn = (Button) findViewById(R.id.MedicationLogBtn);
        exerciseLogBtn = (Button) findViewById(R.id.ExerciseLogBtn);
        extraNotesLogBtn = (Button) findViewById(R.id.ExtraNotesLogBtn);
        questionnaireBtn = (Button) findViewById(R.id.QuestionnaireBtn);
        backButton = (Button) findViewById(R.id.BacktoMainBtn);


        /* Blood sugar button opens blood sugar log page */
        bloodSugarLogBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),BloodSugar.class));
            }
        });

        /* Food button opens food log page */
        foodLogBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),FoodLog.class));
            }
        });

        /* Medication button opens medication log page */
        medicationLogBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),MedLog.class));
            }
        });

        /* Exercise button opens exercise log page */
        exerciseLogBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),ExerciseLog.class));
            }
        });

        /* Extra Notes button opens extra notes log page */
        extraNotesLogBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),ExtraNotes.class));
            }
        });

        /* Questionnaire button opens questionnaire log page */
        questionnaireBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),Questionnaire.class));
            }
        });

        /* Back button leads back to main menu */
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),MainActivity.class));
            }
        });
    }
}