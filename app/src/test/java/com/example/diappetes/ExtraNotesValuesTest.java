package com.example.diappetes;

import android.util.Log;

import junit.framework.TestCase;

import org.junit.Test;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ExtraNotesValuesTest extends TestCase {

    @Test
    public void testGettingAndSetting() {
        ExtraNotesValues log = new ExtraNotesValues();
        try {
            Date time = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss").parse("2021-10-10 10:10:10");
            String note = "Some time";
            log.setTime(time);
            log.setNotes(note);
            assertEquals(time, log.getTime());
            assertEquals(note, log.getNotes());

        } catch (ParseException e) {
            Log.d("ParseException", e.toString());
        }
    }
}