package com.example.mamn01_project.ui.exercises;

import android.annotation.SuppressLint;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Parcel;
import android.util.Log;

import androidx.annotation.NonNull;


public class WalkStepsExercise extends Exercise {

    private final float TARGET_STEPS = 20;

    /**Tydligen kommer stegräknaren att ge antalet steg sedan telefonen startades om så vi behöver
     * denna variabeln */

    private float initialSteps;
    private float currentSteps;

    private boolean completed;

    private Sensor stepSensor;


    public WalkStepsExercise(String name, SensorManager manager) {
        super(name, manager);
        this.initialSteps = -1;
        this.currentSteps = 0;
        this.completed = false;
        stepSensor = manager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER);
        manager.registerListener((SensorEventListener) this, stepSensor, SensorManager.SENSOR_DELAY_NORMAL);
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
        Log.d("WalkStepExercise.processSensorEvent()","curentSteps " + currentSteps + ". Sensor type (19 e rätt): " + sensorEvent.sensor.getType());

        if (sensorEvent.sensor.getType() == Sensor.TYPE_STEP_COUNTER) {
            if (initialSteps == -1) {
                initialSteps = sensorEvent.values[0];
            }
            currentSteps = (sensorEvent.values[0] - initialSteps);
            Log.d("BeachWalk", "processSensorEvent: steps " + currentSteps);
            Log.d("BeachWalk", "Completed?: " + isCompleted());
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

    @Override
    public void Pause() {

    }

    @Override
    public void Resume() {

    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        Log.d("sensor changed","AAAAA");
        processSensorEvent(sensorEvent);
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }
}

