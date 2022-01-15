package com.example.diappetes.Activities.HistoryViewClasses;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.diappetes.Activities.History;
import com.example.diappetes.Global;
import com.example.diappetes.R;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;


public class ExerciseLogHistory extends AppCompatActivity {

    Button backButton, searchButton;
    RecyclerView recyclerView;
    FirebaseAuth auth;
    FirebaseFirestore db;
    ExerciseLogAdapter histAdapt;
    TextView datepickStart;//, timepickStart;
    TextView datepickEnd;//, timepickEnd;

    DatePickerDialog.OnDateSetListener startDatelistener;
    DatePickerDialog.OnDateSetListener endDatelistener;

    ArrayList<ExerciseLogValues> logArray;

    //Create date & time constants
    final Calendar calendar= Calendar.getInstance();
    int year = calendar.get(Calendar.YEAR);
    int month = calendar.get(Calendar.MONTH);
    int day = calendar.get(Calendar.DAY_OF_MONTH);


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.history_search);

        datepickStart = findViewById(R.id.datePickStart);
        datepickEnd = findViewById(R.id.datePickEnd);
        searchButton = findViewById(R.id.searchBtn);

        recyclerView = findViewById(R.id.historyRecycler);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        backButton = (Button) findViewById(R.id.BacktoMainBtn);

        auth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        logArray = new ArrayList<ExerciseLogValues>();
        histAdapt = new ExerciseLogAdapter(ExerciseLogHistory.this,logArray);
        recyclerView.setAdapter(histAdapt);


        datepickStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(
                        ExerciseLogHistory.this,R.style.DialogTheme,startDatelistener,year,month,day);
                datePickerDialog.show();
            }
        });

        //Selected date is saved as string and appears on screen
        startDatelistener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                month=month+1;
                String date =day+"-"+month+"-"+year;
                datepickStart.setText(date);
            }
        };


        datepickEnd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(
                        ExerciseLogHistory.this,R.style.DialogTheme,endDatelistener,year,month,day);
                datePickerDialog.show();
            }
        });

        //Selected date is saved as string and appears on screen
        endDatelistener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                month=month+1;
                String date =day+"-"+month+"-"+year;
                datepickEnd.setText(date);
            }
        };

        final Global global = (Global) getApplicationContext();

        Log.d("NHS:num", global.getNhsNum());

        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Start Date for search
                final String startDateString = datepickStart.getText().toString();
                String startTimeStampString = startDateString + " " + "00:00" + ":00";

                //End date for search
                final String endDateString = datepickEnd.getText().toString();
                String endTimeStampString = endDateString + " " + "23:59" + ":00";

                if(TextUtils.isEmpty(startDateString) ){
                    Toast.makeText( ExerciseLogHistory.this, "Please enter a start date and time", Toast.LENGTH_SHORT).show();
                }

                else if(TextUtils.isEmpty(endDateString) ){
                    Toast.makeText( ExerciseLogHistory.this, "Please enter an end date and time", Toast.LENGTH_SHORT).show();
                }

                else {
                    try {
                        Date start = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss").parse(startTimeStampString);
                        Date end = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss").parse(endTimeStampString);
                        Timestamp startTimeStamp = new Timestamp(start);
                        Timestamp endTimeStamp = new Timestamp(end);

                        logArray.clear();

                        db.collection("Patients").document(global.getNhsNum()).collection("ExerciseLog").whereGreaterThan("Time", startTimeStamp).whereLessThan("Time", endTimeStamp)
                                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                                    @Override
                                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException e) {
                                        if (e != null){
                                            Log.e("Firestore error", e.getMessage());
                                            return;
                                        }

                                        for (DocumentChange dc : value.getDocumentChanges()){

                                            if(dc.getType() == DocumentChange.Type.ADDED ) {

                                                logArray.add(dc.getDocument().toObject(ExerciseLogValues.class));
                                            }
                                        }
                                        histAdapt.notifyDataSetChanged();
                                    }
                                });

                    } catch (ParseException e) {
                        Log.d("Parse Date error",e.toString());
                    }
                }
            }
        });

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), History.class));
            }
        });

    }
}