package com.example.mamn01_project.ui.exercises;

import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Parcelable;
import android.util.Log;

import com.example.mamn01_project.FragmentEventListener;

public abstract class Exercise implements SensorEventListener {
    private String name;
    private boolean enabled;
    protected SensorManager sensorManager;
    protected FragmentEventListener listener;

    public Exercise(String name, SensorManager manager, FragmentEventListener listener) {
        this.name = name;
        this.enabled = false;
        this.listener = listener;
        sensorManager = manager;
        Log.d("Exercise", "Start exercise: " + name);
    }

    public abstract int requiredSensorType();

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {

        this.enabled = enabled;
    }
    public abstract boolean isCompleted();

    public abstract void processSensorEvent(SensorEvent sensorEvent);

    public abstract void Pause();

    public abstract void Resume();

}
