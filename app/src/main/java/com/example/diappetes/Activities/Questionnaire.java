package com.example.diappetes.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

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

public class Questionnaire extends AppCompatActivity {
    TextView datepick,timepick;
    Button cancelButton;
    Button saveButton;
    DatePickerDialog.OnDateSetListener datelistener;
    TimePickerDialog.OnTimeSetListener timelistener;
    CheckBox urinationCheck, thirstCheck, weightCheck;
    CheckBox hungerCheck, visionCheck, tinglingCheck;
    EditText sympNotes;
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
        setContentView(R.layout.questoinnaire);

        final Global global = (Global) getApplicationContext();

        datepick = findViewById(R.id.datePick);
        timepick = findViewById(R.id.timePick);
        cancelButton = (Button) findViewById(R.id.CancelBtn);
        saveButton = (Button) findViewById(R.id.SaveBtn);

        urinationCheck = findViewById(R.id.UrinationCheckBox);
        thirstCheck = findViewById(R.id.ThirstCheckBox);
        weightCheck = findViewById(R.id.WeightLossCheckBox);
        hungerCheck = findViewById(R.id.HungerCheckBox);
        visionCheck = findViewById(R.id.VisionCheckBox);
        tinglingCheck = findViewById(R.id.TinglingCheckBox);
        sympNotes = findViewById(R.id.TypeSympNotes);

        //Calendar appears when "Date" TextView is clicked
        datepick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(
                        Questionnaire.this,R.style.DialogTheme,datelistener,year,month,day);
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
                        Questionnaire.this,android.R.style.Theme_Holo_Light_Dialog_NoActionBar_MinWidth,timelistener,hour,minute,true);
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

        // Cancel button to go back to log menu
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),LogMenu.class));
            }
        });

        // Save button to save log info in Firebase
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // action of the save button
                final boolean urinBool = urinationCheck.isChecked();
                final boolean thirstBool = thirstCheck.isChecked();
                final boolean weightBool = weightCheck.isChecked();
                final boolean hungerBool = hungerCheck.isChecked();
                final boolean visionBool = visionCheck.isChecked();
                final boolean tinglingBool = tinglingCheck.isChecked();
                final String sympString = sympNotes.getText().toString();

                final String dateString = datepick.getText().toString();
                final String timeString = timepick.getText().toString();
                String timeStampString = dateString+" "+timeString+":00";

                //Following are checks that all required fields have values
                if(TextUtils.isEmpty(dateString) || TextUtils.isEmpty(timeString)){
                    Toast.makeText( Questionnaire.this, "Please enter date and time", Toast.LENGTH_SHORT).show();
                }


                else {
                    try {
                        Date queDate = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss").parse(timeStampString);
                        Timestamp timestamp = new Timestamp(queDate);

                        auth = FirebaseAuth.getInstance();
                        db = FirebaseFirestore.getInstance();

                        //Create hashmap to submit to database
                        final Map<String, Object> QueData = new HashMap<>();
                        QueData.put("ExtensiveUrination", urinBool);
                        QueData.put("ExtensiveThirst", thirstBool);
                        QueData.put("WeightLoss", weightBool);
                        QueData.put("ExtensiveHunger", hungerBool);
                        QueData.put("VisionChanges", visionBool);
                        QueData.put("TinglingSensation", tinglingBool);
                        QueData.put("NotesOnSymptoms", sympString);
                        QueData.put("Time", timestamp);


                        //Fetch collection of previous collections to get index for new collection
                        db.collection("Patients").document(global.getNhsNum()).collection("Questionnaire").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()) {
                                    int collectionSize = task.getResult().size();
                                    collectionSize++;

                                    //Add new entry to database
                                    db.collection("Patients").document(global.getNhsNum()).collection("Questionnaire").document("QUE" + collectionSize)
                                            .set(QueData)
                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void aVoid) {
                                                    //Tell user data was submitted correctly
                                                    Toast.makeText(Questionnaire.this, "Your entry has been added", Toast.LENGTH_SHORT).show();
                                                    startActivity(new Intent(getApplicationContext(), LogMenu.class));
                                                }
                                            })
                                            .addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {
                                                    //Alert user data submission failed
                                                    Toast.makeText(Questionnaire.this, "Your entry was not added, please try again", Toast.LENGTH_SHORT).show();

                                                }
                                            });
                                } else {
                                    Log.d("DB_Questionnaire", "Error getting documents: ", task.getException());
                                    Toast.makeText(Questionnaire.this, "Your entry was not added, please try again", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });

                    } catch (ParseException e) {
                        Log.d("DB_Questionnaire", e.toString());
                    }
                }


            }
        });

    }
}