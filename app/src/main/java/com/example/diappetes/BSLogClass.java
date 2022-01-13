package com.example.diappetes;

import java.util.Date;

public class BSLogClass {

    Double BS;
    Date Time;
    String EatenIn2h;

    public java.util.Date getTime() {
        return Time;
    }

    public String getEatenIn2h() {
        return EatenIn2h;
    }

    public void setEatenIn2h(String eatenIn2h) {
        EatenIn2h = eatenIn2h;
    }

    public BSLogClass(){}

    public BSLogClass(Date Time, Double BS) {
        this.Time = Time;
        this.BS = BS;
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
