package com.example.mamn01_project.ui.exercises;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.util.Log;

public class LightExercise extends Exercise {
    private static final float LIGHT_THRESHOLD = 1250; // threshold in lux
    private boolean completed;

    private Sensor lightSensor;

    public LightExercise(String name, SensorManager manager) {
        super(name, manager);
        //this.completed = false;
        lightSensor = manager.getDefaultSensor(Sensor.TYPE_LIGHT);
        manager.registerListener((SensorEventListener) this, lightSensor, SensorManager.SENSOR_DELAY_NORMAL);

    }

    @Override
    public int requiredSensorType() {
        return Sensor.TYPE_LIGHT;
    }

    @Override
    public boolean isCompleted() {
        Log.d("Exercise", "isCompleted method called in LightExercise");
        return completed;

    }

    @Override
    public void processSensorEvent(SensorEvent sensorEvent) {
        if (sensorEvent.sensor.getType() == Sensor.TYPE_LIGHT) {
            Log.d("Light value", "processSensorEvent: " + sensorEvent.values[0]);
            if (sensorEvent.values[0] > LIGHT_THRESHOLD) {
                completed = true;
                Log.d("iscompleted?", "It is:" + isCompleted());
            }
        }
    }

    @Override
    public void Pause() {

    }

    @Override
    public void Resume() {

    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        Log.d("sensor changed","AAAAA");
        processSensorEvent(sensorEvent);
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }
}
