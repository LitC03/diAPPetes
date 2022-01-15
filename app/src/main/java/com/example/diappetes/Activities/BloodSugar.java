package com.example.diappetes.Activities;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.diappetes.Global;
import com.example.diappetes.R;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.MessagingException;
import javax.mail.Transport;
import javax.mail.internet.MimeMessage;

public class BloodSugar extends AppCompatActivity {
    TextView datepick,timepick;
    Button cancelButton,saveButton;
    EditText bloodSugarEdit;
    ProgressBar progressBar;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bloodsugar);

        final Global global = (Global) getApplicationContext();


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
                boolean eatenBool = hasEaten.isChecked();
                final String eatenString = String.valueOf(eatenBool);

                //Following are checks that all required fields have values
                if(TextUtils.isEmpty(bsString)){
                    Toast.makeText( BloodSugar.this, "Please enter your blood sugar value", Toast.LENGTH_SHORT).show();
                }

                //To avoid recording a typo
                else if(!isReasonable(bsString)) {
                    Toast.makeText(BloodSugar.this, "Please re-enter the blood sugar value", Toast.LENGTH_LONG).show();
                }

                else if(TextUtils.isEmpty(dateString) || TextUtils.isEmpty(timeString)){
                    Toast.makeText( BloodSugar.this, "Please enter date and time", Toast.LENGTH_SHORT).show();
                }

                else {
                    double bsDouble = Double.parseDouble(bsString);

                    //Check if patient has hyperglucemia
                    Boolean hasHyperglucemia = checkHyperglucemia(bsDouble,eatenBool);

                    Log.d("DB_BLOODSUGAR","Sugar: "+bsDouble+"\nHas eaten in last 2 hours: "
                            +eatenBool+"\nHas hyperglucemia:"+hasHyperglucemia);

                    //If the patient has hyperglucemia, Alert Dialog Box pops up
                    if (hasHyperglucemia){

                        AlertDialog.Builder builder = new AlertDialog.Builder(BloodSugar.this);

                        //Alert patient that an email will be sent to doctor
                        builder.setTitle("Hyperglucemia!");
                        builder.setMessage("The blood sugar levels you have entered suggest you have " +
                                "hyperglucemia. If you continue, an alert will be sent to your doctor");

                        // Dialog Box remains on screen even if user clicks outside of it
                        builder.setCancelable(false);

                        builder.setPositiveButton("Continue", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // When the user clicks Continue button
                                //entry is stored in firebase and email is sent to doctor
                                addSugarFirebase(bsDouble,timeStampString,eatenString,global);
                                sendEmailtoDoctor(bsString,eatenString,timeString,dateString,global);
                            }
                        });

                        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // If user clicks Cancel button dialog box disappears
                                dialog.cancel();
                            }
                        });

                        AlertDialog alertDialog = builder.create();

                        // Show the Alert Dialog box
                        alertDialog.show();

                    }
                    //If patient doesn't have hyperglucemia, data will be stored normally in Firebase
                    else addSugarFirebase(bsDouble,timeStampString,eatenString,global);


                }
            }
        });

        //Set policy for emails
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
    }

    private void addSugarFirebase(double bsDouble, String timeStampString, String eatenString, Global global) {
        try {
            Date bsDate = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss").parse(timeStampString);
            Timestamp timestamp = new Timestamp(bsDate);

            auth = FirebaseAuth.getInstance();
            db = FirebaseFirestore.getInstance();

            //Create hashmap to submit to database
            final Map<String, Object> BS_data = new HashMap<>();
            BS_data.put("BS", bsDouble);
            BS_data.put("Time", timestamp);
            BS_data.put("EatenIn2h", eatenString);

            //Fetch collection of previous collections to get index for new collection
            db.collection("Patients").document(global.getNhsNum()).collection("BloodSugar").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    if (task.isSuccessful()) {
                        int collectionSize = task.getResult().size();
                        collectionSize++;

                        //Add new entry to database
                        db.collection("Patients").document(global.getNhsNum()).collection("BloodSugar").document("BS" + collectionSize)
                                .set(BS_data)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        //Tell user data was submitted correctly
                                        Toast.makeText(BloodSugar.this, "Your entry has been added", Toast.LENGTH_SHORT).show();
                                        startActivity(new Intent(getApplicationContext(), LogMenu.class));
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        //Alert user data submission failed
                                        Toast.makeText(BloodSugar.this, "Your entry was not added, please try again", Toast.LENGTH_SHORT).show();

                                    }
                                });
                    } else {
                        Log.d("DB_BLOODSUGAR", "Error getting documents: ", task.getException());
                        Toast.makeText(BloodSugar.this, "Your entry was not added, please try again", Toast.LENGTH_SHORT).show();
                    }
                }
            });

        } catch (ParseException e) {
            Log.d("DB_BLOODSUGAR", e.toString());
        }
    }

    public boolean checkHyperglucemia(double bsDouble, boolean eatenBool) {
        //Check blood sugar levels for hyperglucemia
        if (bsDouble>11) {
            return true;
        }
        else if ((bsDouble>7)&&!eatenBool) {
            return true;
        }
        else return false;
    }

    private void sendEmailtoDoctor(String bsString, String eatenString, String timeString, String dateString,Global global) {

        //Set sender's details
        final String diaEmail = "dialog.diappetes@gmail.com";
        final String diaPassword = "Dialogapp123";

        //Query Firestore for patient details and their doctors
        db.collection("Patients").document(global.getNhsNum())
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if(task.isSuccessful()) {

                            DocumentSnapshot document = task.getResult();

                            //Obtain users details
                            String fName = document.getString("fName");
                            String lName = document.getString("lName");
                            String userEmail = document.getString("email");

                            try {

                                List<String> emailList = (List<String>) document.get("Alerts");

                                //if "Alerts" array exists in users document but is empty, send email to admin
                                if (emailList.isEmpty()) {
                                    Log.d("DB_BLOODSUGAR","Alerts array was empty, sending email to admin");
                                    send1Email(global, bsString, eatenString, dateString, timeString, userEmail, diaEmail, diaPassword, fName, lName, diaEmail);
                                }
                                //If "Alerts" array exists, send email to every doctor in it
                                for(String docEmail:emailList) {
                                    send1Email(global,bsString,eatenString,dateString,timeString,userEmail,diaEmail,diaPassword,fName,lName,docEmail);
                                }

                            }
                            catch (Exception e)
                            {
                                //If "Alerts" array doesn't exist in users document, send email to admin
                                send1Email(global,bsString,eatenString,dateString,timeString,userEmail,diaEmail,diaPassword,fName,lName,diaEmail);
                            }
                        }
                         else {
                            Log.d("DB_BLOODSUGAR", "Error getting documents: ", task.getException());
                            Toast.makeText(BloodSugar.this, "There was an error with email sending, please try again", Toast.LENGTH_SHORT).show();
                        }
                    }
                });



    }

    private void send1Email(Global global, String bsString, String eatenString, String dateString, String timeString, String userEmail, String diaEmail, String diaPassword, String fName, String lName, String docEmail) {
        //Prepare message for doctors/admin
        String messageToSend =
                "Patient "+fName+" "+lName+" with NHS number "+global.getNhsNum()+
                " has hyperglucemia, please check with them. Details:\nGlucose level: "
                +bsString+ " mmol/L\nHas eaten in the last 2 hours?: "+eatenString+
                "\nDate: "+dateString+"\nTime: "+timeString+"\nPatient's email: "+userEmail;

        Properties props = new Properties();
        props.put("mail.smtp.auth","true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host","smtp.gmail.com");
        props.put("mail.smtp.port","587");

        Session session= Session.getInstance(props,
                new javax.mail.Authenticator(){
                    @Override
                    protected PasswordAuthentication getPasswordAuthentication(){
                        return new PasswordAuthentication(diaEmail,diaPassword);
                    }
                }
        );
        try {
            //Set message for email
            MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress(diaEmail));
            message.setRecipients(MimeMessage.RecipientType.TO, InternetAddress.parse(docEmail));
            message.setSubject("DiaLog Alert from "+fName+" "+lName+" ("+global.getNhsNum()+")");
            message.setText(messageToSend);

            Transport.send(message);
            Toast.makeText(BloodSugar.this, "Email has been sent to your doctor(s)",Toast.LENGTH_SHORT).show();

        } catch (AddressException e) {
            e.printStackTrace();
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }

    public static boolean isReasonable(String str){
        double bloodSugar = 0;
        try {
            bloodSugar = Double.parseDouble(str);
            //If it can be converted to an int, it is numeric
        } catch(NumberFormatException e){
            return false; //If it can't be converted to an int, it is not numeric
        }
        if ( bloodSugar < 0 || bloodSugar > 30){
            return false;
        }
        else {
            return true;
        }
    }
}
