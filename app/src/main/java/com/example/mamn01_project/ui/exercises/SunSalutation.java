package com.example.mamn01_project.ui.exercises;

import android.content.Intent;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.util.Log;

import androidx.localbroadcastmanager.content.LocalBroadcastManager;

public class SunSalutation extends Exercise implements SensorEventListener{
    private boolean overHead;
    private boolean toesTouched;

    private final double FINAL_REPS = 10;
    private double reps;

    private float[] accelerometer = null;
    private float[] magnetic = null;

    float[] rotationMatrix = new float[9];
    float[] inclinationMatrix = new float[9];

    public SunSalutation() {
        super("Sun Salutation");
        overHead = false;
        toesTouched = false;
        reps = 0;
    }


    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        /*if (sensorEvent.sensor.getType() == Sensor.TYPE_LINEAR_ACCELERATION) {
            if (!isCompleted()) {
                float z = sensorEvent.values[2];
                if (!overHead && z > THRESHOLD) {
                    overHead = true;
                } else if (overHead && z < -THRESHOLD) {
                    toesTouched = true;
                }
                if (overHead && toesTouched) {
                    setCompleted(true);
                }
                Log.d("SunSalutation", "onSensorChanged: z=" + sensorEvent.values[2]);
            }
        }*/

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
    }
    
    public double getReps() {
        return reps;
    }


    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {
    }
}
