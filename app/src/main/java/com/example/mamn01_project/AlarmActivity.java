package com.example.mamn01_project;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Build;
import android.os.Bundle;
import android.view.WindowManager;

public class AlarmActivity extends AppCompatActivity implements SensorEventListener {
    private SensorManager sensorManager;
    private  Sensor accelerometer;

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
            finish();
        }



    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }
    /*Metoden ska återaktivera sensorn när vi har haft applikationen i bakgrunden ett tag eller liknande */
    @Override
    protected void onResume() {
        super.onResume();
        sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    protected void onPause() {
        super.onPause();
        sensorManager.unregisterListener(this);
    }
}