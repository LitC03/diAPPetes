package com.example.diappetes.Activities.HistoryViewClasses;

import java.util.Date;

public class MedLogValues {
    String MedType;
    String Dose;
    Date Time;

    //Empty constructor
    public MedLogValues(){}

    //Getters and setters
    public String getMedType() {
        return MedType;
    }

    public void setMedType(String medType) {
        MedType = medType;
    }

    public String getDose() {
        return Dose;
    }

    public void setDose(String dose) {
        Dose = dose;
    }

    public Date getTime() {
        return Time;
    }

    public void setTime(Date time) {
        Time = time;
    }
}
