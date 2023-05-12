package com.example.mamn01_project.ui.exercises;

import android.annotation.SuppressLint;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.os.Parcel;
import android.util.Log;

import androidx.annotation.NonNull;


public class WalkStepsExercise extends Exercise {

    private final float TARGET_STEPS = 10;

    /**Tydligen kommer stegräknaren att ge antalet steg sedan telefonen startades om så vi behöver
     * denna variabeln */

    private float initialSteps;
    private float currentSteps;

    private boolean completed;


    public WalkStepsExercise(String name) {
        super(name);
        this.initialSteps = -1;
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
        if(currentSteps >= TARGET_STEPS) {
            return true;
        }
        return false;
    }
    @Override
    public void processSensorEvent(SensorEvent sensorEvent) {
        //Ser till så att det är rätt sensor som kommer in
        if (sensorEvent.sensor.getType() == Sensor.TYPE_STEP_COUNTER) {
            if (initialSteps == -1) {
                initialSteps = sensorEvent.values[0];
            }
            currentSteps = sensorEvent.values[0] - initialSteps;
            Log.d("BeachWalk", "processSensorEvent: steps " + currentSteps);
        }

        //Alexander o Jacob
        /*if (sensorEvent.sensor.getType() == Sensor.TYPE_STEP_COUNTER) {
            if (sensorEvent.values[0] == TARGET_STEPS) {
                setCompleted(true);
            } else {
                currentSteps = sensorEvent.values[0];
            }
            Log.d("BeachWalk", "processSensorEvent: steg " + sensorEvent.values[0]);
        }*/

    }
}

