package com.example.mamn01_project.ui.exercises;

import android.annotation.SuppressLint;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorManager;
import android.util.Log;

import androidx.annotation.NonNull;


public class SunSalutationExercise extends Exercise {
    private boolean neutralPosition = false;
    private boolean touchedToes = false;
    private boolean phoneOverHead = false;

    private float[] magnetic = null;
    private float[] accelerometer = null;
    private double reps;
    private final double FINAL_REPS = 5;
    private boolean completed = false;



    public SunSalutationExercise(String name) {
        super(name);
    }

    @Override
    public int requiredSensorType() {
        return Sensor.TYPE_ACCELEROMETER;
    }

    @Override
    public boolean isCompleted() {
        return completed;
        //return neutralPosition && touchedToes && phoneOverHead;
    }

    @Override
    public void processSensorEvent(SensorEvent sensorEvent) {
        /*if (sensorEvent.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            float zValue = sensorEvent.values[2];

            if (!neutralPosition && zValue > -2 && zValue < 2) {
                neutralPosition = true;
            } else if (neutralPosition && !touchedToes && zValue > 9) {
                touchedToes = true;
            } else if (neutralPosition && touchedToes && zValue < -9) {
                phoneOverHead = true;
            }
        }*/

        float[] rotationMatrix = new float[9];
        float[] inclinationMatrix = new float[9];

        if ((magnetic != null) && (sensorEvent.sensor.getType() == Sensor.TYPE_LINEAR_ACCELERATION)) {

            SensorManager.getRotationMatrix(rotationMatrix, inclinationMatrix, accelerometer, magnetic);
            int inclination = (int) Math.round(Math.toDegrees(Math.acos(rotationMatrix[8])));
            if (reps == FINAL_REPS) {
                setCompleted(true);
            } else if (inclination < 90) {
                //face up
                reps += 0.5;
            } else if (inclination > 90) {
                //face down
                reps += 0.5;
            }
        //om magnetometern uppdateras, spara värdet
        } else if (sensorEvent.sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD) {
            magnetic = sensorEvent.values;
        }
        Log.d("SunSalutationExercise", "processorSensorEvent: reps" + reps);
    }

    private void setCompleted(boolean setter) {
        completed = setter;
    }
}

