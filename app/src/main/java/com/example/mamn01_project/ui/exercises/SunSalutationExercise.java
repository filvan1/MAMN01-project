package com.example.mamn01_project.ui.exercises;

import android.annotation.SuppressLint;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.util.Log;

import androidx.annotation.NonNull;


public class SunSalutationExercise extends Exercise {
    private boolean neutralPosition = false;
    private boolean touchedToes = false;
    private boolean phoneOverHead = false;

    public SunSalutationExercise(String name) {
        super(name);
    }

    @Override
    public int requiredSensorType() {
        return Sensor.TYPE_ACCELEROMETER;
    }

    @Override
    public boolean isCompleted() {
        return neutralPosition && touchedToes && phoneOverHead;
    }

    @Override
    public void processSensorEvent(SensorEvent sensorEvent) {
        if (sensorEvent.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            float zValue = sensorEvent.values[2];

            if (!neutralPosition && zValue > -2 && zValue < 2) {
                neutralPosition = true;
                Log.d("Sun", "NEUTRAL");
            } else if (neutralPosition && !touchedToes && zValue > 9) {
                touchedToes = true;
                Log.d("Sun", "TOE");
            } else if (neutralPosition && touchedToes && zValue < -9) {
                phoneOverHead = true;
                Log.d("Sun", "HEAD");
            }
        }
    }
}

