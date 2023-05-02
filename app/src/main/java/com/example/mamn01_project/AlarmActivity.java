package com.example.mamn01_project;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

import android.os.Build;
import android.os.Bundle;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.util.Log;
import android.view.WindowManager;
import android.widget.Toast;

import com.example.mamn01_project.ui.alarm.AlarmStopListener;

public class AlarmActivity extends AppCompatActivity implements SensorEventListener {
    private SensorManager sensorManager;
    private  Sensor accelerometer;
    private AlarmStopListener alarmStopListener;

    /*Dessa variabler är temporära här för att testa solhälsning. Denna data ska komma från en
    separat class för varje övning sen */
    private boolean overHead = false;
    private boolean toesTouched = false;

/* Metoden kallas när aktiviteten startas. Kallar ShowWhenLocked() som gör att det kan visas även när
* mobilen är låst. Vi aktiverar sensormanager som tar hand om sensorerna och aktiverar
* accelerometern. Sätter även upp rätt layout när aktiviteten startas */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        showWhenLocked();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm);

        Toast.makeText(this, "Alarm....", Toast.LENGTH_LONG).show();
        Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);

        // Vibrate for 500 milliseconds
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            v.vibrate(VibrationEffect.createOneShot(500, VibrationEffect.DEFAULT_AMPLITUDE));
        } else {
            //deprecated in API 26
            v.vibrate(500);
        }
        Log.d("AlarmActivity", "onCreate");

    }

    /* Metoden ska se till att vi får visa saker trots att mobilen är låst*/

    private void showWhenLocked(){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O_MR1){
            setShowWhenLocked(true);
            setTurnScreenOn(true);
        } else {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED
            | WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);
        }
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        float z = sensorEvent.values[2];
        if(!overHead && z > 9) {
            overHead = true;
        }else if(overHead && z < -9) {
            toesTouched = true;
        }
        if(overHead && toesTouched) {
           if(alarmStopListener != null) {
               alarmStopListener.onStopAlarm();
           }
        }
        Log.d("AlarmActivity", "onSensorChanged: z=" + sensorEvent.values[2]);


    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }
    /*Metoden ska återaktivera sensorn när vi har haft applikationen i bakgrunden ett tag eller liknande */
    @Override
    protected void onResume() {
        super.onResume();
        sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_NORMAL);
        Log.d("AlarmActivity", "onResume");
    }

    @Override
    protected void onPause() {
        super.onPause();
        sensorManager.unregisterListener(this);
        Log.d("AlarmActivity", "onPause");
    }
    public void setAlarmStopListener(AlarmStopListener listener) {
        this.alarmStopListener = listener;
    }
}