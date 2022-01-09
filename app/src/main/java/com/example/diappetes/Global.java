package com.example.diappetes;

import android.app.Application;


public class Global extends Application {
//place to store the variables that need to persist between different activities
    public String getNhsNum() {
        return nhsNum;
    }

    public void setNhsNum(String nhsNum) {
        this.nhsNum = nhsNum;
    }

    public String getUID() {
        return UID;
    }

    public void setUID(String UID) {
        this.UID = UID;
    }

    private static String UID;
    private static String nhsNum;


}
