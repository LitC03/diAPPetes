package com.example.diappetes;

import java.util.Date;

public class ExerciseLogValues {
    Date Time;
    String Duration;
    String ExerciseType;

    public ExerciseLogValues(){}

    public Date getTime() {
        return Time;
    }

    public void setTime(Date time) {
        Time = time;
    }

    public String getDuration() {
        return Duration;
    }

    public void setDuration(String duration) {
        Duration = duration;
    }

    public String getExerciseType() {
        return ExerciseType;
    }

    public void setExerciseType(String exerciseType) {
        ExerciseType = exerciseType;
    }
}
