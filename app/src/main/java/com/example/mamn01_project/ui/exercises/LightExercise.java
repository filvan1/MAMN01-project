package com.example.mamn01_project.ui.exercises;

import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.MediaPlayer;
import android.os.Handler;
import android.os.Vibrator;
import android.util.Log;
import android.widget.TextView;

import com.example.mamn01_project.FragmentEventListener;
import com.example.mamn01_project.R;

public class LightExercise extends Exercise {
    private static final float LIGHT_THRESHOLD = 1250; // threshold in lux
    private boolean completed;

    private Sensor lightSensor;
    private Vibrator vibrator;

    private TextView indicator;
    private MediaPlayer mediaPlayer;

    public LightExercise(String name, SensorManager manager, TextView indicator, FragmentEventListener listener, Vibrator vibrator) {
        super(name, manager, listener);
        this.completed = false;
        this.vibrator = vibrator;
        this.indicator = indicator;
        mediaPlayer = MediaPlayer.create(indicator.getContext(), R.raw.task_success);
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

                sensorManager.unregisterListener(this);

                indicator.setTextColor(Color.parseColor("#228B22"));
                mediaPlayer.start();

                vibrator.vibrate(250);

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        listener.onEvent();
                    }
                }, 2000);

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
