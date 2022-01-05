package com.example.diappetes;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.TimePicker;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Calendar;
import java.util.Locale;

public class BloodSugar extends AppCompatActivity {
    TextView datepick,timepick;
    Button cancelButton;
    Button saveButton;
    DatePickerDialog.OnDateSetListener datelistener;
    TimePickerDialog.OnTimeSetListener timelistener;

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
        setContentView(R.layout.bloodsugar);

        datepick = findViewById(R.id.datePick);
        timepick = findViewById(R.id.timePick);
        cancelButton = findViewById(R.id.cancelBtn);



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
                String date =day+"/"+month+"/"+year;
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
                startActivity(new Intent(getApplicationContext(),LogBook.class));
            }
        });
    }
}
