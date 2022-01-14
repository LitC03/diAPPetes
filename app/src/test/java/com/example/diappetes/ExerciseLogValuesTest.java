package com.example.diappetes;

import android.util.Log;

import junit.framework.TestCase;

import org.junit.Test;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ExerciseLogValuesTest extends TestCase {

    @Test
    public void testGettingAndSetting() {
        ExerciseLogValues log = new ExerciseLogValues();
        try {
            Date time = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss").parse("2021-10-10 10:10:10");
            String duration = "Some time";
            String type = "Running";
            log.setTime(time);
            log.setDuration(duration);
            log.setExerciseType(type);
            assertEquals(time, log.getTime());
            assertEquals(duration, log.getDuration());
            assertEquals(type, log.getExerciseType());

        } catch (ParseException e) {
            Log.d("ParseException", e.toString());
        }
    }
}
