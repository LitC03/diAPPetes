package com.example.diappetes;

import java.util.Date;

public class FoodLogClass {
    String Calories;
    String Carbs;
    String Meal;
    String Sugars;
    Date Time;

    public FoodLogClass(){}

    public String getCalories() {
        return Calories;
    }

    public void setCalories(String calories) {
        Calories = calories;
    }

    public String getCarbs() {
        return Carbs;
    }

    public void setCarbs(String carbs) {
        Carbs = carbs;
    }

    public String getMeal() {
        return Meal;
    }

    public void setMeal(String meal) {
        Meal = meal;
    }

    public String getSugars() {
        return Sugars;
    }

    public void setSugars(String sugars) {
        Sugars = sugars;
    }

    public Date getTime() {
        return Time;
    }

    public void setTime(Date time) {
        Time = time;
    }
}
