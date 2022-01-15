package com.example.diappetes.Activities.HistoryViewClasses;

import java.util.Date;

public class QuestionnaireClassValues {
    Boolean ExtensiveHunger;
    Boolean ExtensiveThirst;
    Boolean ExtensiveUrination;
    Boolean TinglingSensation;
    Boolean VisionChanges;
    Boolean WeightLoss;
    String NotesOnSymptoms;
    Date Time;

    //Empty constructor
    public QuestionnaireClassValues(){}

    //Getters and setters
    public Boolean getExtensiveHunger() {
        return ExtensiveHunger;
    }

    public void setExtensiveHunger(Boolean extensiveHunger) {
        ExtensiveHunger = extensiveHunger;
    }

    public Boolean getExtensiveThirst() {
        return ExtensiveThirst;
    }

    public void setExtensiveThirst(Boolean extensiveThirst) {
        ExtensiveThirst = extensiveThirst;
    }

    public Boolean getExtensiveUrination() {
        return ExtensiveUrination;
    }

    public void setExtensiveUrination(Boolean extensiveUrination) {
        ExtensiveUrination = extensiveUrination;
    }

    public Boolean getTinglingSensation() {
        return TinglingSensation;
    }

    public void setTinglingSensation(Boolean tinglingSensation) {
        TinglingSensation = tinglingSensation;
    }

    public Boolean getVisionChanges() {
        return VisionChanges;
    }

    public void setVisionChanges(Boolean visionChanges) {
        VisionChanges = visionChanges;
    }

    public Boolean getWeightLoss() {
        return WeightLoss;
    }

    public void setWeightLoss(Boolean weightLoss) {
        WeightLoss = weightLoss;
    }

    public String getNotesOnSymptoms() {
        return NotesOnSymptoms;
    }

    public void setNotesOnSymptoms(String notesOnSymptoms) {
        NotesOnSymptoms = notesOnSymptoms;
    }

    public Date getTime() {
        return Time;
    }

    public void setTime(Date time) {
        Time = time;
    }
}
