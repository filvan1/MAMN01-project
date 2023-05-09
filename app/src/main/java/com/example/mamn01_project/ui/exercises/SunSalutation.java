package com.example.mamn01_project.ui.exercises;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;

public class SunSalutation extends Exercise implements SensorEventListener {
    private float highTarget;
    private float lowTarget;


    public SunSalutation() {
        super("Sun Salutation");
    }

    @Override
    public void run() {

    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {
    }
}
