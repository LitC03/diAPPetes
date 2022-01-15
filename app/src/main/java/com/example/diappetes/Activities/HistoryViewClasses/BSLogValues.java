package com.example.diappetes.Activities.HistoryViewClasses;

import java.util.Date;

public class BSLogValues {
    //All the fields for the blood sugar log
    Double BS;
    Date Time;
    String EatenIn2h;

    //Constructors
    public BSLogValues(){}

    public BSLogValues(Date Time, Double BS) {
        this.Time = Time;
        this.BS = BS;
    }

    //Getters and setters
    public java.util.Date getTime() {
        return Time;
    }

    public String getEatenIn2h() {
        return EatenIn2h;
    }

    public void setEatenIn2h(String eatenIn2h) {
        EatenIn2h = eatenIn2h;
    }

    public void setTime(Date Time) {
        this.Time = Time;
    }

    public Double getBS() {
        return BS;
    }

    public void setBS(Double BS) {
        this.BS = BS;
    }
}
