package com.example.mamn01_project.ui.exercises;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.util.Log;

public class BeachWalk extends Exercise implements SensorEventListener{
    private final float TARGET_STEPS = 20;
    private float currentSteps;


    public BeachWalk() {
        super("BeachWalk");
        this.currentSteps = 0;
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        if (sensorEvent.sensor.getType() == Sensor.TYPE_STEP_COUNTER) {
            if (sensorEvent.values[0] == TARGET_STEPS) {
                setCompleted(true);
            } else {
                currentSteps = sensorEvent.values[0];
            }
            Log.d("BeachWalk", "onSensorChanged: steg " + sensorEvent.values[0]);
        }
    }

    public float getCurrentSteps() {
        return currentSteps;
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {
    }

}
