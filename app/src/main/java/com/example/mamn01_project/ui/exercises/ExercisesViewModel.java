package com.example.mamn01_project.ui.exercises;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;
import java.util.List;

public class ExercisesViewModel extends ViewModel {

    private MutableLiveData <List<ExerciseListEntry>> exercises;

    public ExercisesViewModel() {
        exercises = new MutableLiveData<>();
        List<ExerciseListEntry> startingExercises = new ArrayList<>();
        startingExercises.add(new ExerciseListEntry("Solhälsning"));
        startingExercises.add(new ExerciseListEntry("Beachwalk"));

        exercises.setValue(startingExercises);


    }

    public LiveData<List<ExerciseListEntry>> getExercises() {
        return exercises;

    }

    public void setExercises(List<ExerciseListEntry> list){
        exercises.setValue(list);
    }
}