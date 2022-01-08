package com.example.diappetes;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import javax.annotation.Nullable;

import static java.util.logging.Logger.global;

public class BloodSugar extends AppCompatActivity {
    TextView datepick,timepick;
    Button cancelButton,saveButton;
    EditText bloodSugarEdit;
    DatePickerDialog.OnDateSetListener datelistener;
    TimePickerDialog.OnTimeSetListener timelistener;
    SwitchCompat hasEaten;

    FirebaseAuth auth;
    FirebaseFirestore db;

    //Create date & time constants
    final Calendar calendar= Calendar.getInstance();
    int year = calendar.get(Calendar.YEAR);
    int month = calendar.get(Calendar.MONTH);
    int day = calendar.get(Calendar.DAY_OF_MONTH);
    int hour = calendar.get(Calendar.HOUR_OF_DAY);
    int minute = calendar.get(Calendar.MINUTE);

    //final Global global = (Global) getApplicationContext();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bloodsugar);

        final Global global = (Global) getApplicationContext();


//        Intent n = getIntent();
//        final Global global = (Global)n.getSerializableExtra("global");

//        Log.d("DB_BLOODSUGAR",""+global.getNhsNum());

        datepick = findViewById(R.id.datePick);
        timepick = findViewById(R.id.timePick);
        cancelButton = findViewById(R.id.cancelBtn);
        saveButton = findViewById(R.id.saveBtn);
        bloodSugarEdit = findViewById(R.id.SugarField);
        hasEaten = findViewById(R.id.hourSwitch);


        //Calendar appears when "Date" TextView is clicked
        datepick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(
                        BloodSugar.this,R.style.DialogTheme,datelistener,year,month,day);
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
                        BloodSugar.this,android.R.style.Theme_Holo_Light_Dialog_NoActionBar_MinWidth,timelistener,hour,minute,true);
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
            public void onClick(View v){
                startActivity(new Intent(getApplicationContext(),LogMenu.class));
            }
        });

        saveButton.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View v){

                final String bsString = bloodSugarEdit.getText().toString();
                final String dateString = datepick.getText().toString();
                final String timeString = timepick.getText().toString();
                String timeStampString = dateString+" "+timeString+":00";
                double bsDouble = Double.parseDouble(bsString);
                boolean eatenBool = hasEaten.isChecked();
                final String eatenString = String.valueOf(eatenBool);

                try {
                    Log.d("DB_BLOODSUGAR",""+global.getNhsNum());
                    Date bsDate = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss").parse(timeStampString);

                    Log.d("DB",""+bsDate);

                    Timestamp timestamp = new Timestamp(bsDate);

                    Log.d("DB",""+timestamp);

                    auth = FirebaseAuth.getInstance();
                    db = FirebaseFirestore.getInstance();

                    final Map<String, Object> BS_data = new HashMap<>();
                    BS_data.put("BS", bsDouble);
                    BS_data.put("Time", timestamp);
                    BS_data.put("EatenIn2h", eatenString);

                    db.collection("Patients").document(global.getNhsNum()).collection("BloodSugar").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                Log.d("DB_BLOODSUGAR", ""+task.getResult().size());
                                int collectionSize = task.getResult().size();
                                collectionSize++;
                                db.collection("Patients").document(global.getNhsNum()).collection("BloodSugar").document("BS"+collectionSize)
                                        .set(BS_data)
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                Log.d("DB_BLOODSUGAR","Inserted BS");
                                                Toast.makeText(BloodSugar.this, "Your entry has been added", Toast.LENGTH_SHORT).show();
                                                startActivity(new Intent(getApplicationContext(), MainActivity.class));
                                            }
                                        })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                Log.d("TAG",e.toString());
                                            }
                                        });
                            } else {
                                Log.d("DB_BLOODSUGAR", "Error getting documents: ", task.getException());
                            }
                        }
                    });

                } catch (ParseException e) {
                    Log.d("DB_BLOODSUGAR",e.toString());
                }
            }
        });
    }
}
