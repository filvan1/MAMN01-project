package com.example.mamn01_project;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;

import com.example.mamn01_project.ui.alarm.AlarmFragment;
import com.example.mamn01_project.ui.exercises.Exercise;
import com.example.mamn01_project.ui.exercises.ExercisesViewModel;
import com.example.mamn01_project.ui.exercises.WalkStepsExercise;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.mamn01_project.databinding.ActivityWakeBinding;

import java.util.Arrays;
import java.util.List;

public class Wake extends AppCompatActivity {

    private ActivityWakeBinding binding;
    private BroadcastReceiver alarmStopReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityWakeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        BottomNavigationView navView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_alarm, R.id.navigation_exercises)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_wake);
        //NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(binding.navView, navController);

        if (!Settings.canDrawOverlays(this)){
            // Permission is not granted, ask the user to enable it
            Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                    Uri.parse("package:" + getPackageName()));

        }

        registerReceiver(receiver, new IntentFilter(getResources().getString(R.string.intent_alarm_trigger)));

        ExercisesViewModel model = new ViewModelProvider(this).get(ExercisesViewModel.class);

        model.getExercises();

        List<Exercise> exercisePool = Arrays.asList(
                new WalkStepsExercise("beachWalk", 20)
                // Lägg till fler exercises här
        );

        //LocalBroadcastManager.getInstance(this).registerReceiver(alarmStopReceiver, new IntentFilter("ALARM_STOP"));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(alarmStopReceiver);
    }

    BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            // Set up the separate alarm screen Activity
            Intent nextActivity = new Intent(context, AlarmActivity.class);

            // Make sure that the alarm activity starts properly
            nextActivity.setFlags(
                    Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED
                            | Intent.FLAG_ACTIVITY_PREVIOUS_IS_TOP
                            | Intent.FLAG_ACTIVITY_REORDER_TO_FRONT
                            | Intent.FLAG_ACTIVITY_NEW_TASK
            );
            PendingIntent pending = PendingIntent.getActivity(context, 0, nextActivity, PendingIntent.FLAG_IMMUTABLE);

            try {
                pending.send();
            } catch (PendingIntent.CanceledException e) {
                e.printStackTrace();
            }
        }
    };
}
