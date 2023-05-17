package com.example.mamn01_project.ui.exercises;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Vibrator;
import android.util.Log;
import android.widget.TextView;

import com.example.mamn01_project.FragmentEventListener;

public class LightExercise extends Exercise {
    private static final float LIGHT_THRESHOLD = 1250; // threshold in lux
    private boolean completed;

    private Sensor lightSensor;
    private Vibrator vibrator;

    private TextView lumenTarget;

    public LightExercise(String name, SensorManager manager, TextView lumen, FragmentEventListener listener, Vibrator vibrator) {
        super(name, manager, listener);
        //this.completed = false;
        this.vibrator = vibrator;
        this.lumenTarget = lumen;
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
                vibrator.vibrate(100);
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                if(isCompleted()) {
                    sensorManager.unregisterListener(this);
                    Log.d("iscompleted?", "It is:" + isCompleted());
                    listener.onEvent();
                }
            }
        }
    }

    @Override
    public void Pause() {
        sensorManager.unregisterListener(this);
    }

    @Override
    public void Resume() {
        sensorManager.registerListener((SensorEventListener) this, lightSensor, SensorManager.SENSOR_DELAY_NORMAL);
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
