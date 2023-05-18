package com.example.mamn01_project.ui.exercises;

import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Handler;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.util.Log;
import android.widget.TextView;

import com.example.mamn01_project.FragmentEventListener;
import com.example.mamn01_project.R;


public class WalkStepsExercise extends Exercise {

    private final float TARGET_STEPS = 10;

    /**Tydligen kommer stegräknaren att ge antalet steg sedan telefonen startades om så vi behöver
     * denna variabeln */

    private float initialSteps;
    private float currentSteps;

    private boolean completed;

    private Sensor stepSensor;
    private TextView repTextTarget;
    private Vibrator vibrator;

    private MediaPlayer mediaPlayer;



    public WalkStepsExercise(String name, SensorManager manager, TextView repText, FragmentEventListener listener, Vibrator vibrator) {
        super(name, manager, listener);
        this.initialSteps = -1;
        this.currentSteps = 0;
        this.completed = false;
        this.vibrator = vibrator;
        this.repTextTarget = repText;
        repTextTarget.setText(""+(int)TARGET_STEPS);

        mediaPlayer = MediaPlayer.create(repText.getContext(), R.raw.task_success);

        stepSensor = manager.getDefaultSensor(Sensor.TYPE_STEP_DETECTOR);
        manager.registerListener((SensorEventListener) this, stepSensor, SensorManager.SENSOR_DELAY_NORMAL);
    }
    public void updateSteps(int steps) {
        this.currentSteps += steps;
    }

    public float getCurrentSteps() {
        return currentSteps;
    }
    private void setCompleted(boolean b) {
        completed = true;
    }

    @Override
    public int requiredSensorType() {
        return Sensor.TYPE_STEP_DETECTOR;
    }

    @Override
    public boolean isCompleted() {
        if(currentSteps >= TARGET_STEPS) {
            return true;
        }
        return false;
    }
    @Override
    public void processSensorEvent(SensorEvent sensorEvent) {
        //Ser till så att det är rätt sensor som kommer in
        Log.d("WalkStepExercise.processSensorEvent()","curentSteps " + currentSteps + ". Sensor type (18 e rätt): " + sensorEvent.sensor.getType());

        if (sensorEvent.sensor.getType() == Sensor.TYPE_STEP_DETECTOR) {
            if (initialSteps == -1) {
                initialSteps = sensorEvent.values[0];
            }
            currentSteps++;
            repTextTarget.setText(""+(int)(TARGET_STEPS-currentSteps));
            //vibrator.vibrate(100);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                vibrator.vibrate(VibrationEffect.createOneShot(100, 30));
            }


            Log.d("BeachWalk", "processSensorEvent: steps " + currentSteps + "  A "+ sensorEvent.values);

            if(isCompleted()){
                sensorManager.unregisterListener(this);
                //repTextTarget.setTextColor(Color.parseColor("#228B22")); //Snyggare men mindre synligt
                repTextTarget.setTextColor(Color.GREEN); //Fulare men synligare
                mediaPlayer.start();

                vibrator.vibrate(250);

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        listener.onEvent();
                    }
                }, 2000);
            }

            Log.d("BeachWalk", "Completed?: " + isCompleted());
        }

        //Alexander o Jacob
        /*if (sensorEvent.sensor.getType() == Sensor.TYPE_STEP_COUNTER) {
            if (sensorEvent.values[0] == TARGET_STEPS) {
                setCompleted(true);
            } else {
                currentSteps = sensorEvent.values[0];
            }
            Log.d("BeachWalk", "processSensorEvent: steg " + sensorEvent.values[0]);
        }*/

    }

    @Override
    public void Pause() {
        sensorManager.unregisterListener(this);
    }

    @Override
    public void Resume() {
        sensorManager.registerListener((SensorEventListener) this, stepSensor, SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        processSensorEvent(sensorEvent);
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }
}

