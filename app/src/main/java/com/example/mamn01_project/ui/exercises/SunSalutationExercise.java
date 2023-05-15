package com.example.mamn01_project.ui.exercises;

import android.annotation.SuppressLint;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Vibrator;
import android.util.Log;

import androidx.annotation.NonNull;


public class SunSalutationExercise extends Exercise {

    private float[] magnetic;
    private float[] acceleration;
    private float pitch;
    private float roll;
    private float[] inclineGravity = new float[3];
    private Sensor accelerometer;
    private Sensor magnetometer;


    private double reps;
    private final double FINAL_REPS = 10;
    private boolean completed = false;
    private Vibrator vibrator;
    private Context context;

    private enum Orientation {
        UP,
        DOWN
    }
    private Orientation lastRep;



    public SunSalutationExercise(String name, SensorManager manager, Context context) {
        super(name, manager);
        lastRep = Orientation.UP;
        accelerometer = manager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        magnetometer = manager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
        this.context = context;
        vibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
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
        //Log.d("SunSalutationExercise", "processorSensorEvent: reps " + reps + ". Sensor (10: acc, 2: mag): " + sensorEvent.sensor.getType());
        if (reps == FINAL_REPS) {
            setCompleted(true);
            Log.d("SunSalutationExercise.processSensorEvent()", "EXERCISE COMPLETED");
        } else if (sensorEvent.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            acceleration = sensorEvent.values;
            //Log.d("Sensor type accelerometer","magnetometer values: " + magnetic);
            //Log.d("Sensor type accelerometer","accelerometer values" + acceleration);

        } else if (sensorEvent.sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD) {

            magnetic = sensorEvent.values;
            //Log.d("Sensor type magnetic", "magnetometer values: " +  magnetic);
            //Log.d("Sensor type magnetic", "accelerometer values: " +  acceleration);
            if (isTiltUpward() && !isTiltDownward() && lastRep == Orientation.DOWN ) {
                //TODO vibration!
                vibrator.vibrate(100);
                reps += 0.5;
                lastRep = Orientation.UP;
                Log.d("SunSalutationExercise.processSensorEvent()", "Tilt up. reps completed: " + reps);
            } else if (isTiltDownward() && !isTiltUpward() && lastRep == Orientation.UP) {
                //TODO vibration!
                vibrator.vibrate(100);
                reps += 0.5;
                lastRep = Orientation.DOWN;
                Log.d("SunSalutationExercise.processSensorEvent()", "Tilt down. reps completed: " + reps);
            }
        }

    }

    private boolean isTiltUpward() {
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

                if (roll < 0 && ((objPitchZeroResult > 0 && objPitchZeroPointTwoResult > 0) || (objPitchZeroResult < 0 && objPitchZeroPointTwoNegativeResult > 0)) && (inclination > 30 && inclination < 40))
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

    private boolean isTiltDownward() {
        if (acceleration != null && magnetic != null) {
            //Log.d("isTiltDownward", "tild down: acceleration+magnetic");
            float R[] = new float[9];
            float I[] = new float[9];

            boolean success = SensorManager.getRotationMatrix(R, I, acceleration, magnetic);

            if (success) {
                //Log.d("isTiltDownward", "tilt down success");

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

                //Checks if device is flat on groud or not
                int inclination = (int) Math.round(Math.toDegrees(Math.acos(inclineGravity[2])));

                Float objPitch = new Float(pitch);
                Float objZero = new Float(0.0);
                Float objZeroPointTwo = new Float(0.2);
                Float objZeroPointTwoNegative = new Float(-0.2);

                int objPitchZeroResult = objPitch.compareTo(objZero);
                int objPitchZeroPointTwoResult = objZeroPointTwo.compareTo(objPitch);
                int objPitchZeroPointTwoNegativeResult = objPitch.compareTo(objZeroPointTwoNegative);

                if (roll < 0 && ((objPitchZeroResult > 0 && objPitchZeroPointTwoResult > 0) || (objPitchZeroResult < 0 && objPitchZeroPointTwoNegativeResult > 0)) && (inclination > 140 && inclination < 170))
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

    }

    @Override
    public void Resume() {

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

