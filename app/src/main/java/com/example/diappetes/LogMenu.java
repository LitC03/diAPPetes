package com.example.diappetes;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class LogMenu extends AppCompatActivity {

    Button bloodSugarLogBtn;
    Button foodLogBtn;
    Button medicationLogBtn;
    Button exerciseLogBtn;
    Button extraNotesLogBtn;

    Button questionnaireBtn;
    Button backtoMainBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.log_menu);

        /* Blood sugar button opens blood sugar log page */
        bloodSugarLogBtn = (Button) findViewById(R.id.BloodSugarLogBtn);
        bloodSugarLogBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),BloodSugar.class));
            }
        });

        /* Food button opens food log page */
        foodLogBtn = (Button) findViewById(R.id.FoodLogBtn);
        foodLogBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),FoodLog.class));
            }
        });

        /* Medication button opens medication log page */
        medicationLogBtn = (Button) findViewById(R.id.MedicationLogBtn);
        medicationLogBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),MedLog.class));
            }
        });

        /* Exercise button opens exercise log page */
        exerciseLogBtn = (Button) findViewById(R.id.ExerciseLogBtn);
        exerciseLogBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),ExerciseLog.class));
            }
        });

        /* Extra Notes button opens extra notes log page */
        extraNotesLogBtn = (Button) findViewById(R.id.ExtraNotesLogBtn);
        extraNotesLogBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),ExtraNotes.class));
            }
        });

        /* Questionnaire button opens questionnaire log page */
        questionnaireBtn = (Button) findViewById(R.id.QuestionnaireBtn);
        questionnaireBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),Questionnaire.class));
            }
        });

        /* Back button leads back to main menu */
        backtoMainBtn = (Button) findViewById(R.id.BacktoMainBtn);
        backtoMainBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),MainActivity.class));
            }
        });
    }
}