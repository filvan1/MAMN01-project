package com.example.mamn01_project;

import android.Manifest;
import android.app.Activity;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.PowerManager;
import android.provider.Settings;
import android.util.Log;
import android.widget.TextView;

import com.example.mamn01_project.ui.alarm.AlarmFragment;
import com.example.mamn01_project.ui.alarm.AlarmViewModel;
import com.example.mamn01_project.ui.exercises.Exercise;
import com.example.mamn01_project.ui.exercises.ExerciseListEntry;
import com.example.mamn01_project.ui.exercises.ExercisesViewModel;
import com.example.mamn01_project.ui.exercises.WalkStepsExercise;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContract;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.mamn01_project.databinding.ActivityWakeBinding;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
/**
 * Wake är huvudaktiviteten i applikationen. Den sköter också navigationen mellan
 * alarm och exercise fragmenten.
 *
 * */
public class Wake extends AppCompatActivity {

    private ActivityWakeBinding binding;
    private BroadcastReceiver alarmStopReceiver;
    private ExercisesViewModel exercisesViewModel;
    public AlarmViewModel alarmViewModel;

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

        /* Ask for permission to draw overlays*/
        if (!Settings.canDrawOverlays(this)){
            // Permission is not granted, ask the user to enable it

            ActivityResultLauncher<Intent> someActivityResultLauncher = registerForActivityResult(
                    new ActivityResultContracts.StartActivityForResult(),
                    new ActivityResultCallback<ActivityResult>() {
                        @Override
                        public void onActivityResult(ActivityResult result) {
                            if (!Settings.canDrawOverlays(getApplicationContext())) {
                                android.os.Process.killProcess(android.os.Process.myPid());
                            }
                        }
                    });
            Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                    Uri.parse("package:" + getPackageName()));
            someActivityResultLauncher.launch(intent);
        }

        /* Ask for permission to not be optimized */
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            Intent intent = new Intent();
            String packageName = getPackageName();
            PowerManager pm = (PowerManager) getSystemService(POWER_SERVICE);
            if (!pm.isIgnoringBatteryOptimizations(packageName)) {
                intent.setAction(Settings.ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS);
                intent.setData(Uri.parse("package:" + packageName));
                ActivityResultLauncher<Intent> someActivityResultLauncher = registerForActivityResult(
                        new ActivityResultContracts.StartActivityForResult(),
                        new ActivityResultCallback<ActivityResult>() {
                            @Override
                            public void onActivityResult(ActivityResult result) {
                                if (!pm.isIgnoringBatteryOptimizations(packageName)) {
                                    android.os.Process.killProcess(android.os.Process.myPid());
                                }
                            }
                        });
                someActivityResultLauncher.launch(intent);
            }

        }

        if(ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACTIVITY_RECOGNITION) == PackageManager.PERMISSION_DENIED){
            //ask for permission
            requestPermissions(new String[]{Manifest.permission.ACTIVITY_RECOGNITION}, 326);
        }

        registerReceiver(receiver, new IntentFilter(getResources().getString(R.string.intent_alarm_trigger)));

        /**
         * Initialliserar en exerciseViewModel genom att skapa en instans av ExerciseViewModel.
         * */
        exercisesViewModel = new ViewModelProvider(this).get(ExercisesViewModel.class);
        //model.getExercises();


        /**
         * Initialliserar en observer för att upptäcka förändringar i enabled exercises. Metoden tar två argument
         * först den som den som äger aktiviteten(lifecycle owner) vilket är Wake(this) och en function som exekveras
         * om data förändras.
         * */
        exercisesViewModel.getExercises().observe(this, enabledExercises -> {
            // You can use the enabledExercises list here
            // For example, update your exercisePool variable based on the enabled exercises
            for (ExerciseListEntry exercise : enabledExercises) {
                Log.d("EnabledExercises", exercise.getName() + ": " + (exercise.isEnabled() ? "Enabled" : "Disabled"));
            }
        });

        alarmViewModel = new ViewModelProvider(this).get(AlarmViewModel.class);


        alarmViewModel.getAlarm().observe(this, alarm -> {
            Log.d("AlarmGet", "Observed changed text: "+ alarm);
            TextView alarmText = findViewById(R.id.text_home);
            alarmText.setText(alarm);
        });

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
            /**
             * Hämtar först en lista av enabled exercises med hjälp av vår viewmodel som vi etablerade oven i klassen.
             * Om det finns enabled exercises så skapar vi en arraylist med namnen på alla exercises. Sedan skickar
             * vi iväg detta med ett intent till AlarmActivity. (Läs kommentar i onCreate och createExerciseByName i
             * AlarmActivity där datan behandlas.)
             * */
            List<ExerciseListEntry> enabledExercises = exercisesViewModel.getExercises().getValue();
            if (enabledExercises != null) {
                ArrayList<String> enabledExerciseNames = new ArrayList<>();
                for (ExerciseListEntry exercise : enabledExercises) {

                    Log.d("AlarmActivity", exercise.getName()+" in list with status "+ exercise.isEnabled() );
                    if(exercise.isEnabled()){

                        Log.d("AlarmActivity", exercise.getName()+" enabled" );
                        enabledExerciseNames.add(exercise.getName());
                    }
                }
                nextActivity.putStringArrayListExtra("enabledExerciseNames", enabledExerciseNames);

                Log.d("AlarmActivity", "ADDED: " +enabledExerciseNames );
            }


            // Make sure that the alarm activity starts properly
            nextActivity.setFlags(
                    Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED
                            | Intent.FLAG_ACTIVITY_PREVIOUS_IS_TOP
                            | Intent.FLAG_ACTIVITY_REORDER_TO_FRONT
                            | Intent.FLAG_ACTIVITY_NEW_TASK
            );
            PendingIntent pending;
            pending = PendingIntent.getActivity(context, 0, nextActivity, PendingIntent.FLAG_MUTABLE | PendingIntent.FLAG_UPDATE_CURRENT);

            try {
                TextView alarmText = findViewById(R.id.text_home);
                alarmViewModel.setAlarmString(alarmViewModel.ALARM_NOT_SET);
                alarmText.setText(alarmViewModel.ALARM_NOT_SET);
                pending.send();
            } catch (PendingIntent.CanceledException e) {
                e.printStackTrace();
            }
        }
    };
}
