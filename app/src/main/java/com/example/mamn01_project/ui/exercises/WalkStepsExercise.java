package com.example.mamn01_project.ui.exercises;

public class WalkStepsExercise extends Exercise {
    private int targetSteps;
    private int currentSteps;


    public WalkStepsExercise(String name, int targetSteps) {
        super(name);
        this.currentSteps = 0;
        this.targetSteps = targetSteps;
    }
    public void updateSteps(int steps) {
        this.currentSteps += steps;
    }

    @Override
    public boolean isCompleted() {
        return false;
    }
}
