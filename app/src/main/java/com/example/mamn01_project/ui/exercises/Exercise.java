package com.example.mamn01_project.ui.exercises;

import android.hardware.SensorEvent;
import android.os.Parcelable;

public abstract class Exercise {
    private String name;
    private boolean enabled;

    public Exercise(String name) {
        this.name = name;
        this.enabled = false;
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

    public void processSensorEvent(SensorEvent sensorEvent) {

    }
}
