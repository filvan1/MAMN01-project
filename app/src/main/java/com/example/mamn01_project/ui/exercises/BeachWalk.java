package com.example.mamn01_project.ui.exercises;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;

public class BeachWalk extends Exercise implements SensorEventListener {
    private int targetSteps;
    private int currentSteps;


    public BeachWalk() {
        super("BeachWalk");
        this.currentSteps = 0;
        this.targetSteps = 20;
    }
    public void updateSteps(int steps) {
        this.currentSteps += steps;
    }


    @Override
    protected void run() {

    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }
}
