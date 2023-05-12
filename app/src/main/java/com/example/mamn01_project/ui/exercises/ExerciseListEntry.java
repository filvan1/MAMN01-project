package com.example.mamn01_project.ui.exercises;

import android.hardware.SensorEvent;

public class ExerciseListEntry {
    private String name;
    private boolean enabled;

    public ExerciseListEntry(String name) {
        this.name = name;
        this.enabled = false;
    }

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

}
