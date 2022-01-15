package com.example.diappetes.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.diappetes.Activities.HistoryViewClasses.BSHistory;
import com.example.diappetes.Activities.HistoryViewClasses.ExerciseLogHistory;
import com.example.diappetes.Activities.HistoryViewClasses.ExtraNotesHistory;
import com.example.diappetes.Activities.HistoryViewClasses.FoodHistory;
import com.example.diappetes.Activities.HistoryViewClasses.MedHistory;
import com.example.diappetes.Activities.HistoryViewClasses.QuestionnaireHistory;
import com.example.diappetes.R;

public class History extends AppCompatActivity {

    Button backButton,graphButton, bsHistButton;
    Button foodHistButton, medHistButton, exHistButton;
    Button noteHistButton, queHistButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.history);

        backButton = (Button) findViewById(R.id.BacktoMainBtn);
        graphButton = (Button) findViewById(R.id.graphingBtn);
        bsHistButton = (Button) findViewById(R.id.bsHistory);
        foodHistButton = (Button) findViewById(R.id.foodHistory);
        medHistButton = (Button) findViewById(R.id.medHistory);
        exHistButton = (Button) findViewById(R.id.exerciseHistory);
        noteHistButton = (Button) findViewById(R.id.extraNotesHist);
        queHistButton = (Button) findViewById(R.id.questionnaireHist);

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),MainActivity.class));
            }
        });

        graphButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),Graph.class));
            }
        });

        bsHistButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), BSHistory.class));
            }
        });

        foodHistButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), FoodHistory.class));
            }
        });

        medHistButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), MedHistory.class));
            }
        });

        exHistButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), ExerciseLogHistory.class));
            }
        });

        noteHistButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), ExtraNotesHistory.class));
            }
        });

        queHistButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), QuestionnaireHistory.class));
            }
        });
    }
}