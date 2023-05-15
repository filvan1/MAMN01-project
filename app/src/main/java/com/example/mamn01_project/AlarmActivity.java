package com.example.mamn01_project;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import android.content.Context;

import android.content.Intent;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

import android.os.Build;
import android.os.Bundle;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;
import android.media.MediaPlayer;

import com.example.mamn01_project.ui.exercises.Exercise;
import com.example.mamn01_project.ui.exercises.ExerciseFragment;
import com.example.mamn01_project.ui.exercises.ExerciseListEntry;
import com.example.mamn01_project.ui.exercises.SunSalutationExercise;
import com.example.mamn01_project.ui.exercises.WalkStepsExercise;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class AlarmActivity extends AppCompatActivity {
    private SensorManager sensorManager;
    private  Sensor accelerometer;

    private Sensor gyroscope;

    private Sensor stepCounter;
    private MediaPlayer mediaPlayer;



    private FragmentManager fragmentManager;
    private FragmentTransaction currentTransaction;

    private List<String> enabledExercises;
    private String currentExercise;
    private ExerciseFragment frag;

    /**
     * Metoden kallas när aktiviteten startas, då börjar den med att aktivera
     * sensormanager, accelerometer, gyro, step counter och media player. Den tar sedan emot alla enabled
     * exercises från ett intent skickas från Wake när AlarmActivity startas.
     * Intentet används helt enkelt för att skicka en lista av enabled exercises från
     * Wake aktiviteten till AlarmActivity. Sedan går vi igenom den listan i nästa kodblock
     * och väljer ut en av dem. Under det aktiverar vi ljudet när alarmet går.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        gyroscope = sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);
        stepCounter = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER);
        showWhenLocked();
        super.onCreate(savedInstanceState);
        /* Create a */
        fragmentManager = getSupportFragmentManager();
        currentTransaction = fragmentManager.beginTransaction();
        /* Set empty fragment container as current view*/
        setContentView(R.layout.activity_alarm);

        if (getIntent().hasExtra("enabledExerciseNames")) {
            List<String> enabledExerciseNames = getIntent().getStringArrayListExtra("enabledExerciseNames");
            Log.d("AlarmActivity", "RECEIVED: " +enabledExerciseNames );
            if (enabledExerciseNames != null && !enabledExerciseNames.isEmpty()) {
                enabledExercises = new ArrayList<>();
                for (String exerciseName : enabledExerciseNames) {

                    Log.d("AlarmActivity", "NEW EXERCISE"+ exerciseName);
                    enabledExercises.add(exerciseName);

                }
            }
        }

        /* I don't know why this if statement is needed*/
        if (savedInstanceState == null){
            frag = ExerciseFragment.newInstance(null, R.layout.fragment_alert);
            frag.setOnEventListener(listener);
            currentTransaction
                    .setReorderingAllowed(true)
                    .add(R.id.alert_container, frag)
                    .commit();
        }

        mediaPlayer = MediaPlayer.create(this, R.raw.waveswav);
        if(mediaPlayer != null) {
            mediaPlayer.start();
        } else {
            Log.e("AlarmActivity", "Error creating media player");
        }
        //Till knappen när larmet går
        /*
        Button exercise_start = findViewById(R.id.start_button);
        exercise_start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Inte implementerat ännu
            }
        });

        */
        Toast.makeText(this, "Alarm....", Toast.LENGTH_LONG).show();
        Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);

        // Vibrate for 500 milliseconds
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            v.vibrate(VibrationEffect.createOneShot(500, VibrationEffect.DEFAULT_AMPLITUDE));
        } else {
            //deprecated in API 26
            v.vibrate(500);
        }

    }

    private FragmentEventListener listener = new FragmentEventListener(){
        @Override
        public void onEvent(){
            Log.d("AlarmActivity", "AAAAAAAAAA");
            if (enabledExercises != null && !enabledExercises.isEmpty()) {
                Random random = new Random();
                currentExercise = enabledExercises.remove(random.nextInt(enabledExercises.size()));
                Log.d("AlarmActivity", "Current exercise: " + currentExercise);

                //fragmentManager = getSupportFragmentManager();
                frag = ExerciseFragment.newInstance(currentExercise, R.layout.fragment_exercise);
                frag.setOnEventListener(listener);
                currentTransaction = fragmentManager.beginTransaction();
                currentTransaction
                        .setReorderingAllowed(true)
                        .replace(R.id.alert_container, frag)
                        .commit();
            } else {
                finish();
            }
        }
    };


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

    /* När overHead && toesTouched båda är sanna skapar vi en broadcast för att stoppa alarmet
    * i Wake har vi en braodcast reviecer som kan fånga upp detta och onRecieve metoden körs. Den
    * kommer i sin tur att kalla på stopAlarm metoden i AlarmFragment. stopAlarm metoden stoppar
    * alarmet och skickar tillbaka braodcast att alarmet är stoppat.  */


    /*Metoden ska återaktivera sensorn när vi har haft applikationen i bakgrunden ett tag eller liknande */
    @Override
    protected void onResume() {
        super.onResume();
        if(frag != null) frag.Resume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if(frag != null) frag.Pause();
        Log.d("AlarmActivity", "onPause");
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        this.finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }
}