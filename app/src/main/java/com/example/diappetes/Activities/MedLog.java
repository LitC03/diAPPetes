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

public class MedLog extends AppCompatActivity {

    Button cancelButton, saveButton;
    TextView datepick, timepick;
    EditText medTypeEdit, doseEdit;
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
        setContentView(R.layout.medication_log);

        final Global global = (Global) getApplicationContext();

        // Cancel button to go back to log menu
        cancelButton = (Button) findViewById(R.id.cancelBtn);
        saveButton = findViewById(R.id.saveBtn);
        datepick = findViewById(R.id.datePick);
        timepick = findViewById(R.id.timePick);
        medTypeEdit = findViewById(R.id.TypeField);
        doseEdit = findViewById(R.id.DoseField);

        //Calendar appears when "Date" TextView is clicked
        datepick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(
                        MedLog.this,R.style.DialogTheme,datelistener,year,month,day);
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
                        MedLog.this,android.R.style.Theme_Holo_Light_Dialog_NoActionBar_MinWidth,timelistener,hour,minute,true);
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

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),LogMenu.class));
            }
        });

        saveButton.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View V){
                //Fetch entered values
                final String medTypeString = medTypeEdit.getText().toString();
                final String doseString = doseEdit.getText().toString();
                final String dateString = datepick.getText().toString();
                final String timeString = timepick.getText().toString();
                String timeStampString = dateString + " " + timeString + ":00";

                //Following are checks that all required fields have values
                if(TextUtils.isEmpty(doseString) || TextUtils.isEmpty(medTypeString)){
                    Toast.makeText( MedLog.this, "Please enter your medication and dose", Toast.LENGTH_SHORT).show();
                }

                else if(TextUtils.isEmpty(dateString) || TextUtils.isEmpty(timeString)){
                    Toast.makeText( MedLog.this, "Please enter date and time", Toast.LENGTH_SHORT).show();
                }

                else {
                    try {
                        Date date = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss").parse(timeStampString);
                        Timestamp timestamp = new Timestamp(date);

                        auth = FirebaseAuth.getInstance();
                        db = FirebaseFirestore.getInstance();

                        //Create hashmap for database
                        final Map<String, Object> Med_data = new HashMap<>();
                        Med_data.put("MedType", medTypeString);
                        Med_data.put("Dose", doseString);
                        Med_data.put("Time", timestamp);

                        //Fetch collection of previous collections to get index for new collection
                        db.collection("Patients").document(global.getNhsNum()).collection("MedLog").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()) {
                                    int collectionSize = task.getResult().size();
                                    collectionSize++;

                                    //Add new entry to database
                                    db.collection("Patients").document(global.getNhsNum()).collection("MedLog").document("ML" + collectionSize)
                                            .set(Med_data)
                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void aVoid) {
                                                    Toast.makeText(MedLog.this, "Your entry has been added", Toast.LENGTH_SHORT).show();
                                                    startActivity(new Intent(getApplicationContext(), LogMenu.class));
                                                }
                                            })
                                            .addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {
                                                    Toast.makeText(MedLog.this, "Failed to save entry", Toast.LENGTH_SHORT).show();
                                                }
                                            });
                                }
                            }

                        });
                    } catch (ParseException e) {
                        Log.d("DB_Medlog",e.toString());
                    }
                }
            }

        });

    }
}
