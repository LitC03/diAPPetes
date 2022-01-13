package com.example.diappetes;

import java.util.Date;

public class MedLogClass {
    String MedType;
    String Dose;
    Date Time;

    public MedLogClass(){}

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
