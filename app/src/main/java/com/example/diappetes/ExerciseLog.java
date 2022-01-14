package com.example.diappetes;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class ExerciseLog extends AppCompatActivity {
    TextView datepick, timepick;
    Button cancelButton, saveButton;
    EditText typeEdit, durEdit;
    DatePickerDialog.OnDateSetListener datelistener;
    TimePickerDialog.OnTimeSetListener timelistener;

    FirebaseAuth auth;
    FirebaseFirestore db;

    //Create date & time constants
    final Calendar calendar= Calendar.getInstance();
    int year = calendar.get(Calendar.YEAR);
    int month = calendar.get(Calendar.MONTH);
    int day = calendar.get(Calendar.DAY_OF_MONTH);
    int hour = calendar.get(Calendar.HOUR_OF_DAY);
    int minute = calendar.get(Calendar.MINUTE);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.exercise_log);

        final Global global = (Global) getApplicationContext();

        datepick = findViewById(R.id.datePick);
        timepick = findViewById(R.id.timePick);
        cancelButton = findViewById(R.id.cancelBtn);
        saveButton = findViewById(R.id.saveBtn);
        typeEdit = findViewById(R.id.TypeField);
        durEdit = findViewById(R.id.DurationField);


        //Calendar appears when "Date" TextView is clicked
        datepick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(
                        ExerciseLog.this,R.style.DialogTheme,datelistener,year,month,day);
                datePickerDialog.show();
            }
        });

        //Selected date is saved as string and appears on screen
        datelistener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                month=month+1;
                String date =day+"-"+month+"-"+year;
                datepick.setText(date);
            }
        };


        //Clock appears on screen
        timepick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TimePickerDialog timePickerDialog = new TimePickerDialog(
                        ExerciseLog.this,android.R.style.Theme_Holo_Light_Dialog_NoActionBar_MinWidth,timelistener,hour,minute,true);
                timePickerDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
                timePickerDialog.show();
            }
        });

        //Selected time is saved as string and appears on screen
        timelistener = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int hour, int minute) {
                String time = hour+":"+minute;
                timepick.setText(time);
            }
        };

        //Cancel to go back to log menu
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),LogMenu.class));
            }
        });

        //Save button
        saveButton.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View V){
                //Fetch all entered data
                final String exerciseTypeString = typeEdit.getText().toString();
                final String durString = durEdit.getText().toString();
                final String dateString = datepick.getText().toString();
                final String timeString = timepick.getText().toString();
                String timeStampString = dateString + " " + timeString + ":00";

                //Following are checks that all required fields have values
                if(TextUtils.isEmpty(exerciseTypeString) || TextUtils.isEmpty(durString)){
                    Toast.makeText( ExerciseLog.this, "Please enter the type of exercise and duration", Toast.LENGTH_SHORT).show();
                }

                else if(TextUtils.isEmpty(dateString) || TextUtils.isEmpty(timeString)){
                    Toast.makeText( ExerciseLog.this, "Please enter date and time", Toast.LENGTH_SHORT).show();
                }

                else {
                    try {
                        Date date = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss").parse(timeStampString);
                        Timestamp timestamp = new Timestamp(date);

                        //Create an instance of the firebase
                        auth = FirebaseAuth.getInstance();
                        db = FirebaseFirestore.getInstance();

                        //Create hashmap to submit to firebase
                        final Map<String, Object> Med_data = new HashMap<>();
                        Med_data.put("ExerciseType", exerciseTypeString);
                        Med_data.put("Duration", durString);
                        Med_data.put("Time", timestamp);

                        //Fetch collection of previous logs to get index for new log
                        db.collection("Patients").document(global.getNhsNum()).collection("ExerciseLog").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()) {
                                    int collectionSize = task.getResult().size();
                                    collectionSize++;

                                    //Submit new log to database
                                    db.collection("Patients").document(global.getNhsNum()).collection("ExerciseLog").document("EL" + collectionSize)
                                            .set(Med_data)
                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void aVoid) {
                                                    Toast.makeText(ExerciseLog.this, "Your entry has been added", Toast.LENGTH_SHORT).show();
                                                    startActivity(new Intent(getApplicationContext(), MainActivity.class));
                                                }
                                            })
                                            .addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {
                                                    Toast.makeText(ExerciseLog.this, "Failed to save entry", Toast.LENGTH_SHORT).show();
                                                }
                                            });
                                }
                            }

                        });
                    }
                    catch (ParseException e) {
                        Log.d("DB_Exercise Log",e.toString());
                    }
                }
            }
        });
    }
}