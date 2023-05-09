package com.example.mamn01_project;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;

import com.example.mamn01_project.ui.alarm.AlarmFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.mamn01_project.databinding.ActivityWakeBinding;

public class Wake extends AppCompatActivity {

    private ActivityWakeBinding binding;
    private BroadcastReceiver alarmStopReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityWakeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        createNotificationChannel();

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
        alarmStopReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment_activity_wake);
                Fragment fragment = navHostFragment.getChildFragmentManager().getFragments().get(0);
                if (fragment instanceof AlarmFragment) {
                    ((AlarmFragment) fragment).stopAlarm();
                }
            }
        };
        //LocalBroadcastManager.getInstance(this).registerReceiver(alarmStopReceiver, new IntentFilter("ALARM_STOP"));
    }


        private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "mamn01";
            String desc = "Channel for Alarm Manager";
            int imp = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel channel = new NotificationChannel("mamn01", name, imp);
            channel.setDescription(desc);
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(alarmStopReceiver);
    }
}
