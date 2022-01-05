package com.example.diappetes;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.TimePicker;

import java.util.Calendar;

public class Questionnaire extends AppCompatActivity {
    TextView datepick,timepick;
    Button CancelBtn;
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
        setContentView(R.layout.questoinnaire);

        CancelBtn = (Button) findViewById(R.id.CancelBtn);
        CancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),LogMenu.class));
            }
        });

        datepick = findViewById(R.id.datePick);
        timepick = findViewById(R.id.timePick);

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
                String date =day+"/"+month+"/"+year;
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



    }
}