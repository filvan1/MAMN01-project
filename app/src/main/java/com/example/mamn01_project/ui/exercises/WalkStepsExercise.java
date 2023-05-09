package com.example.mamn01_project.ui.exercises;

import android.annotation.SuppressLint;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.os.Parcel;
import android.util.Log;

import androidx.annotation.NonNull;

@SuppressLint("ParcelCreator")
public class WalkStepsExercise extends Exercise {
    private final float TARGET_STEPS = 20;
    private float currentSteps;

    private boolean completed;


    public WalkStepsExercise(String name, int targetSteps) {
        super(name);
        this.currentSteps = 0;
        this.completed = false;
    }
    public void updateSteps(int steps) {
        this.currentSteps += steps;
    }

    public float getCurrentSteps() {
        return currentSteps;
    }
    private void setCompleted(boolean b) {
        completed = true;
    }

    @Override
    public int requiredSensorType() {
        return Sensor.TYPE_STEP_COUNTER;
    }

    @Override
    public boolean isCompleted() {
        return completed;
    }
    @Override
    public void processSensorEvent(SensorEvent sensorEvent) {
        //Måste få in rätt sensor här från alarmactivity
        if (sensorEvent.sensor.getType() == Sensor.TYPE_STEP_COUNTER) {
            if (sensorEvent.values[0] == TARGET_STEPS) {
                setCompleted(true);
            } else {
                currentSteps = sensorEvent.values[0];
            }
            Log.d("BeachWalk", "processSensorEvent: steps " + sensorEvent.values[0]);
        }
    }
}
