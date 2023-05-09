package com.example.mamn01_project.ui.exercises;

import android.annotation.SuppressLint;
import android.hardware.Sensor;
import android.os.Parcel;

import androidx.annotation.NonNull;


public class SunSalutationExercise extends Exercise{
    private float highTarget;
    private float lowTarget;


    public SunSalutationExercise(String name) {
        super(name);
    }

    @Override
    public int requiredSensorType() {
       return  Sensor.TYPE_GYROSCOPE;
    }

    @Override
    public boolean isCompleted() {
        return false;
    }
}
