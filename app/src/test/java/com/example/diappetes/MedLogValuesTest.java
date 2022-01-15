package com.example.diappetes;

import android.util.Log;

import com.example.diappetes.Activities.HistoryViewClasses.MedLogValues;

import junit.framework.TestCase;

import org.junit.Test;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MedLogValuesTest extends TestCase {

    @Test
    public void testGettingAndSetting() {
        MedLogValues log = new MedLogValues();
        try {
            Date time = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss").parse("2021-10-10 10:10:10");
            String dose = "Some dose";
            String medType = "Some Type";
            log.setTime(time);
            log.setDose(dose);
            log.setMedType(medType);
            assertEquals(time, log.getTime());
            assertEquals(dose, log.getDose());
            assertEquals(medType, log.getMedType());

        } catch (ParseException e) {
            Log.d("ParseException", e.toString());
        }
    }
}