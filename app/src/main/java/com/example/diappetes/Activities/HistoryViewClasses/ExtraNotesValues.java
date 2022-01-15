package com.example.diappetes.Activities.HistoryViewClasses;

import java.util.Date;

public class ExtraNotesValues {
    String Notes;
    Date Time;
    //Empty constructors
    public ExtraNotesValues(){}
    //Getters and setters
    public String getNotes() {
        return Notes;
    }

    public void setNotes(String notes) {
        Notes = notes;
    }

    public Date getTime() {
        return Time;
    }

    public void setTime(Date time) {
        Time = time;
    }
}
