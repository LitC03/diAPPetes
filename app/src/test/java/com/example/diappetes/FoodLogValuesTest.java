package com.example.diappetes;

import android.util.Log;

import junit.framework.TestCase;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class FoodLogValuesTest extends TestCase {

    public void testGettingAndSetting() {
        FoodLogValues log = new FoodLogValues();
        try {
            Date time = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss").parse("2021-10-10 10:10:10");
            String Calories = "1000";
            String Carbs = "100";
            String Meal = "Pasta";
            String Sugars = "10";
            log.setTime(time);
            log.setCalories(Calories);
            log.setCarbs(Carbs);
            log.setMeal(Meal);
            log.setSugars(Sugars);
            assertEquals(time, log.getTime());
            assertEquals(Calories, log.getCalories());
            assertEquals(Carbs, log.getCarbs());
            assertEquals(Meal, log.getMeal());
            assertEquals(Sugars, log.getSugars());

        } catch (ParseException e) {
            Log.d("ParseException", e.toString());
        }
    }

}