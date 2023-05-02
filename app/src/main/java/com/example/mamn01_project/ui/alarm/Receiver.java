package com.example.mamn01_project.ui.alarm;


import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.example.mamn01_project.AlarmActivity;
import com.example.mamn01_project.R;

public class Receiver extends BroadcastReceiver {

    // Triggers when alarm broadcast is sent from AlarmFragment
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
}
