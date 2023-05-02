package com.example.mamn01_project.ui.exercises;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;
import java.util.List;

public class ExercisesViewModel extends ViewModel {

    private MutableLiveData <List<Exercise>> exercises;

    public ExercisesViewModel() {
        exercises = new MutableLiveData<>();
        List<Exercise> startingExercises = new ArrayList<>();
        startingExercises.add(new Exercise("Solhälsning"));
        startingExercises.add(new Exercise("Krigaren"));
        startingExercises.add(new Exercise("Ljus"));
        startingExercises.add(new Exercise("Övning"));
        startingExercises.add(new Exercise("Övning2"));



        exercises.setValue(startingExercises);
    }

    public LiveData<List<Exercise>> getExercises() {
        return exercises;

    }
}