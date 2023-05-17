package com.example.mamn01_project.ui.exercises;

import android.annotation.SuppressLint;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Vibrator;
import android.util.Log;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.mamn01_project.FragmentEventListener;


public class SunSalutationExercise extends Exercise {

    private float[] magnetic;
    private float[] acceleration;
    private float pitch;
    private float roll;
    private float[] inclineGravity = new float[3];
    private Sensor accelerometer;
    private Sensor magnetometer;


    private double reps;
    private final int FINAL_REPS = 12;
    private boolean completed = false;
    private Vibrator vibrator;
    private TextView repTextTarget;

    private enum Orientation {
        UP,
        DOWN
    }
    private Orientation lastRep;


    public SunSalutationExercise(String name, SensorManager manager, TextView repText, FragmentEventListener listener, Vibrator vibrator) {
        super(name, manager, listener);
        lastRep = Orientation.UP;
        this.vibrator = vibrator;
        repTextTarget = repText;
        repTextTarget.setText(""+(int)FINAL_REPS);
        accelerometer = manager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        //accelerometer = manager.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION);
        magnetometer = manager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
        manager.registerListener((SensorEventListener) this, accelerometer, SensorManager.SENSOR_DELAY_NORMAL);
        manager.registerListener((SensorEventListener) this, magnetometer, SensorManager.SENSOR_DELAY_NORMAL);
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

        //V1: orienteringsbaserad, inte bara acceleration uppåt--------------------------------
        /*
        float[] rotationMatrix = new float[9];
        float[] inclinationMatrix = new float[9];

        Log.d("SunSalutationEcercise","Sensor type:" + sensorEvent.sensor.getType());
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
        */


        //V2: orientering igen, funkar faktiskt, men fett finicky----------------------------
        //Log.d("SunSalutationExercise", "processorSensorEvent: reps " + reps + ". Sensor (10: acc, 2: mag): " + sensorEvent.sensor.getType());
        if (reps == FINAL_REPS) {
            setCompleted(true);
            sensorManager.unregisterListener(this);
            listener.onEvent();
            Log.d("SunSalutationExercise.processSensorEvent()", "EXERCISE COMPLETED");
        } else if (sensorEvent.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            acceleration = sensorEvent.values;
            //Log.d("Sensor type accelerometer","magnetometer values: " + magnetic);
            //Log.d("Sensor type accelerometer","accelerometer values" + acceleration);

        } else if (sensorEvent.sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD) {

            magnetic = sensorEvent.values;
            //Log.d("Sensor type magnetic", "magnetometer values: " +  magnetic);
            //Log.d("Sensor type magnetic", "accelerometer values: " +  acceleration);
            if (isDown() && !isUp() && lastRep == Orientation.UP) {
                vibrator.vibrate(100);
                reps += 1;
                lastRep = Orientation.DOWN;
                repTextTarget.setText(""+(int)(FINAL_REPS-reps));
                Log.d("SunSalutationExercise.processSensorEvent()", "Tilt up. reps completed: " + reps);
            } else if (isUp() && !isDown() && lastRep == Orientation.DOWN) {
                vibrator.vibrate(100);
                reps += 1;
                lastRep = Orientation.UP;
                repTextTarget.setText(""+(int)(FINAL_REPS-reps));
                Log.d("SunSalutationExercise.processSensorEvent()", "Tilt down. reps completed: " + reps);
            }

            if(isCompleted()){
                sensorManager.unregisterListener(this);
                listener.onEvent();
            }
        }

    }

    private boolean isDown() { //(tiltUpward)
        if (acceleration != null && magnetic!= null) {
            //Log.d("isTiltUpward", "tiled up: acceleration+magnetic");
            float R[] = new float[9];
            float I[] = new float[9];

            boolean success = SensorManager.getRotationMatrix(R, I, acceleration, magnetic);

            if (success) {
                //Log.d("isTiltUpward", "tilt up success");
                float orientation[] = new float[3];
                SensorManager.getOrientation(R, orientation);

                /*
                 * If the roll is positive, you're in reverse landscape (landscape right), and if the roll is negative you're in landscape (landscape left)
                 *
                 * Similarly, you can use the pitch to differentiate between portrait and reverse portrait.
                 * If the pitch is positive, you're in reverse portrait, and if the pitch is negative you're in portrait.
                 *
                 * orientation -> azimut, pitch and roll
                 *
                 *
                 */

                pitch = orientation[1];
                roll = orientation[2];

                inclineGravity = acceleration.clone();

                double norm_Of_g = Math.sqrt(inclineGravity[0] * inclineGravity[0] + inclineGravity[1] * inclineGravity[1] + inclineGravity[2] * inclineGravity[2]);

                // Normalize the accelerometer vector
                inclineGravity[0] = (float) (inclineGravity[0] / norm_Of_g);
                inclineGravity[1] = (float) (inclineGravity[1] / norm_Of_g);
                inclineGravity[2] = (float) (inclineGravity[2] / norm_Of_g);

                //Checks if device is flat on ground or not
                int inclination = (int) Math.round(Math.toDegrees(Math.acos(inclineGravity[2])));

                /*
                 * Float obj1 = new Float("10.2");
                 * Float obj2 = new Float("10.20");
                 * int retval = obj1.compareTo(obj2);
                 *
                 * if(retval > 0) {
                 * System.out.println("obj1 is greater than obj2");
                 * }
                 * else if(retval < 0) {
                 * System.out.println("obj1 is less than obj2");
                 * }
                 * else {
                 * System.out.println("obj1 is equal to obj2");
                 * }
                 */
                Float objPitch = new Float(pitch);
                Float objZero = new Float(0.0);
                Float objZeroPointTwo = new Float(0.2);
                Float objZeroPointTwoNegative = new Float(-0.2);

                int objPitchZeroResult = objPitch.compareTo(objZero);
                int objPitchZeroPointTwoResult = objZeroPointTwo.compareTo(objPitch);
                int objPitchZeroPointTwoNegativeResult = objPitch.compareTo(objZeroPointTwoNegative);

                if (roll < 0 && ((objPitchZeroResult > 0 && objPitchZeroPointTwoResult > 0) || (objPitchZeroResult < 0 && objPitchZeroPointTwoNegativeResult > 0)) && (inclination > 0 && inclination < 40))
                {
                    return true;
                }
                else
                {
                    return false;
                }
            }
        }
        return false;
    }

    private boolean isUp() {
        if (acceleration != null && magnetic != null) {
            float R[] = new float[9];
            float I[] = new float[9];

            boolean success = SensorManager.getRotationMatrix(R, I, acceleration, magnetic);

            if (success) {

                float orientation[] = new float[3];
                SensorManager.getOrientation(R, orientation);

                pitch = orientation[1];
                roll = orientation[2];

                inclineGravity = acceleration.clone();

                double norm_Of_g = Math.sqrt(inclineGravity[0] * inclineGravity[0] + inclineGravity[1] * inclineGravity[1] + inclineGravity[2] * inclineGravity[2]);

                // Normalize the accelerometer vector
                inclineGravity[0] = (float) (inclineGravity[0] / norm_Of_g);
                inclineGravity[1] = (float) (inclineGravity[1] / norm_Of_g);
                inclineGravity[2] = (float) (inclineGravity[2] / norm_Of_g);

                //Checks if device is flat on ground or not
                int inclination = (int) Math.round(Math.toDegrees(Math.acos(inclineGravity[2])));

                Float objPitch = new Float(pitch);
                Float objZero = new Float(0.0);
                Float objZeroPointTwo = new Float(0.2);
                Float objZeroPointTwoNegative = new Float(-0.2);

                int objPitchZeroResult = objPitch.compareTo(objZero);
                int objPitchZeroPointTwoResult = objZeroPointTwo.compareTo(objPitch);
                int objPitchZeroPointTwoNegativeResult = objPitch.compareTo(objZeroPointTwoNegative);

                if (roll < 0 && ((objPitchZeroResult > 0 && objPitchZeroPointTwoResult > 0) || (objPitchZeroResult < 0 && objPitchZeroPointTwoNegativeResult > 0)) && (inclination > 160 && inclination < 180))
                {
                    return true;
                }
                else
                {
                    return false;
                }
            }
        }

        return false;
    }

    @Override
    public void Pause() {
        sensorManager.unregisterListener(this);
    }

    @Override
    public void Resume() {
        sensorManager.registerListener((SensorEventListener) this, accelerometer, SensorManager.SENSOR_DELAY_NORMAL);
        sensorManager.registerListener((SensorEventListener) this, magnetometer, SensorManager.SENSOR_DELAY_NORMAL);
    }

    private void setCompleted(boolean setter) {
        completed = setter;
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        processSensorEvent(sensorEvent);
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }
}

