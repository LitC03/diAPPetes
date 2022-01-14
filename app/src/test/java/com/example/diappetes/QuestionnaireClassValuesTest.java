package com.example.diappetes;

import android.util.Log;

import junit.framework.TestCase;

import org.junit.Test;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class QuestionnaireClassValuesTest extends TestCase {

    @Test
    public void testGettingAndSetting() {
        QuestionnaireClassValues log = new QuestionnaireClassValues();
        try {
            Date time = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss").parse("2021-10-10 10:10:10");
            Boolean thirst = false;
            Boolean hunger = true;
            Boolean urination = false;
            Boolean tingling = true;
            Boolean vision = false;
            Boolean weightLoss = true;
            String otherSymptoms = "Some other symptoms";

            log.setTime(time);
            log.setExtensiveThirst(thirst);
            log.setExtensiveHunger(hunger);
            log.setExtensiveUrination(urination);
            log.setTinglingSensation(tingling);
            log.setVisionChanges(vision);
            log.setWeightLoss(weightLoss);
            log.setNotesOnSymptoms(otherSymptoms);

            assertEquals(time, log.getTime());
            assertEquals(hunger, log.getExtensiveHunger());
            assertEquals(thirst, log.getExtensiveThirst());
            assertEquals(urination, log.getExtensiveUrination());
            assertEquals(tingling, log.getTinglingSensation());
            assertEquals(vision, log.getVisionChanges());
            assertEquals(weightLoss, log.getWeightLoss());
            assertEquals(otherSymptoms, log.getNotesOnSymptoms());

        } catch (ParseException e) {
            Log.d("ParseException", e.toString());
        }
    }
}