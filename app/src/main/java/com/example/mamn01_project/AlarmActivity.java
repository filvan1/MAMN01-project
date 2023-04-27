package com.example.mamn01_project;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Build;
import android.os.Bundle;
import android.view.WindowManager;

public class AlarmActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        showWhenLocked();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm);

    }

    private void showWhenLocked(){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O_MR1){
            setShowWhenLocked(true);
            setTurnScreenOn(true);
        } else {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED
            | WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);
        }
    }
}