package com.example.diappetes.Activities;

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

import com.example.diappetes.Global;
import com.example.diappetes.R;
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

public class ExtraNotes extends AppCompatActivity {
    // Create fields to associate ui components with
    Button cancelButton, saveButton;
    TextView datepick, timepick;
    EditText notesEdit;
    DatePickerDialog.OnDateSetListener datelistener;
    TimePickerDialog.OnTimeSetListener timelistener;
    FirebaseAuth auth;
    FirebaseFirestore db;

    // Create date & time constants
    final Calendar calendar= Calendar.getInstance();
    int year = calendar.get(Calendar.YEAR);
    int month = calendar.get(Calendar.MONTH);
    int day = calendar.get(Calendar.DAY_OF_MONTH);
    int hour = calendar.get(Calendar.HOUR_OF_DAY);
    int minute = calendar.get(Calendar.MINUTE);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.extranotes);

        final Global global = (Global) getApplicationContext();

        // Associating the variables with ui components
        cancelButton = (Button) findViewById(R.id.cancelBtn);
        saveButton = findViewById(R.id.saveBtn);
        datepick = findViewById(R.id.datePick);
        timepick = findViewById(R.id.timePick);
        notesEdit = findViewById(R.id.extraNotes);

        // Calendar appears when "Date" TextView is clicked
        datepick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(
                        ExtraNotes.this,R.style.DialogTheme,datelistener,year,month,day);
                datePickerDialog.show();
            }
        });

        // Selected date is saved as string and appears on screen
        datelistener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                month=month+1;
                String date =day+"-"+month+"-"+year;
                datepick.setText(date);
            }
        };


        // Clock appears on screen
        timepick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TimePickerDialog timePickerDialog = new TimePickerDialog(
                        ExtraNotes.this,android.R.style.Theme_Holo_Light_Dialog_NoActionBar_MinWidth,timelistener,hour,minute,true);
                timePickerDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
                timePickerDialog.show();
            }
        });

        // Selected time is saved as string and appears on screen
        timelistener = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int hour, int minute) {
                String time = hour+":"+minute;
                timepick.setText(time);
            }
        };

        // Save button to get user logged data and send to the database
        saveButton.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View V){
                // Fetch entered values
                final String notesString = notesEdit.getText().toString();
                final String dateString = datepick.getText().toString();
                final String timeString = timepick.getText().toString();
                String timeStampString = dateString + " " + timeString + ":00";

                // Checking that all required fields have values
                if(TextUtils.isEmpty(notesString)){
                    // Informing the user of the missing information
                    Toast.makeText( ExtraNotes.this, "Please do not leave the notes empty", Toast.LENGTH_SHORT).show();
                }

                else if(TextUtils.isEmpty(dateString) || TextUtils.isEmpty(timeString)){
                    Toast.makeText( ExtraNotes.this, "Please enter date and time", Toast.LENGTH_SHORT).show();
                }

                else{
                    try {
                        Date date = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss").parse(timeStampString);
                        Timestamp timestamp = new Timestamp(date);

                        auth = FirebaseAuth.getInstance();
                        db = FirebaseFirestore.getInstance();

                        // Create hashmap for database
                        final Map<String, Object> Notes_data = new HashMap<>();
                        Notes_data.put("Notes", notesString);
                        Notes_data.put("Time", timestamp);

                        // Fetch collection of previous collections to get index for new collection
                        db.collection("Patients").document(global.getNhsNum()).collection("ExtraNotes").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()) {
                                    int collectionSize = task.getResult().size();
                                    collectionSize++;

                                    // Add new entry to database
                                    db.collection("Patients").document(global.getNhsNum()).collection("ExtraNotes").document("EN" + collectionSize)
                                            .set(Notes_data)
                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void aVoid) {
                                                    // Alert user data was submitted correctly
                                                    Toast.makeText(ExtraNotes.this, "Your entry has been added", Toast.LENGTH_SHORT).show();
                                                    startActivity(new Intent(getApplicationContext(), LogMenu.class));
                                                }
                                            })
                                            .addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {
                                                    // Alert user data submission failed
                                                    Toast.makeText(ExtraNotes.this, "Failed to save entry", Toast.LENGTH_SHORT).show();
                                                }
                                            });
                                }
                            }

                        });
                    } catch (ParseException e) {
                        Log.d("DB_ExtraNotes",e.toString());
                    }
                }
            }

        });

        // Cancel button to go back to log menu
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),LogMenu.class));
            }
        });

    }
}