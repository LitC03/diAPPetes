package com.example.diappetes;

import android.util.Log;

import junit.framework.TestCase;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class BSLogValuesTest extends TestCase {

    public void testGettingAndSetting() {
        BSLogValues log = new BSLogValues();
        try {
            Date time = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss").parse("2021-10-10 10:10:10");
            Double bs = 9.4;
            String eaten = "false";
            log.setTime(time);
            log.setEatenIn2h(eaten);
            log.setBS(bs);
            assertEquals(time, log.getTime());
            assertEquals(eaten, log.getEatenIn2h());
            assertEquals(bs, log.getBS());

        } catch (ParseException e) {
            Log.d("ParseException", e.toString());
        }
    }

}