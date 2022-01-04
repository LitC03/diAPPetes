package com.example.diappetes;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class LogMenu extends AppCompatActivity {

    Button BloodSugarLogBtn;
    Button FoodLogBtn;
    Button MedicationLogBtn;
    Button ExerciseLogBtn;
    Button ExtraNotesLogBtn;

    Button QuestionnaireBtn;
    Button BacktoMainBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.log_menu);

        /* Blood sugar button opens blood sugar log page */
        /*BloodSugarLogBtn = (Button) findViewById(R.id.BloodSugarLogBtn);
        BloodSugarLogBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),__.class));
            }
        }); */

        /* Food button opens food log page */
        /*FoodLogBtn = (Button) findViewById(R.id.FoodLogBtn);
        FoodLogBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),__.class));
            }
        }); */

        /* Medication button opens medication log page */
        /*MedicationLogBtn = (Button) findViewById(R.id.MedicationLogBtn);
        MedicationLogBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),__.class));
            }
        }); */

        /* Exercise button opens exercise log page */
        /*ExerciseLogBtn = (Button) findViewById(R.id.ExerciseLogBtn);
        ExerciseLogBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),__.class));
            }
        });*/

        /* Extra Notes button opens extra notes log page */
        /*ExtraNotesLogBtn = (Button) findViewById(R.id.ExtraNotesLogBtn);
        ExtraNotesLogBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),__.class));
            }
        }); */

        /* Questionnaire button opens questionnaire log page */
        QuestionnaireBtn = (Button) findViewById(R.id.QuestionnaireBtn);
        QuestionnaireBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),Questionnaire.class));
            }
        });

        /* Back button leads back to main menu */
        BacktoMainBtn = (Button) findViewById(R.id.BacktoMainBtn);
        BacktoMainBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),MainActivity.class));
            }
        });
    }
}