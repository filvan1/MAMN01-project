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

        Toast.makeText(context, "Alarm....", Toast.LENGTH_LONG).show();
        Vibrator v = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);

        // Vibrate for 500 milliseconds
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            v.vibrate(VibrationEffect.createOneShot(500, VibrationEffect.DEFAULT_AMPLITUDE));
        } else {
            //deprecated in API 26
            v.vibrate(500);
        }

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

        // Tested a push notification as well.
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "mamn01")
                .setSmallIcon(R.drawable.ic_notifications_black_24dp)
                .setContentTitle("ALARM YO")
                .setContentText("AAAAA")
                .setAutoCancel(true)
                .setDefaults(NotificationCompat.DEFAULT_ALL)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setContentIntent(pending);

        NotificationManagerCompat notifManager = NotificationManagerCompat.from(context);

        notifManager.notify(123, builder.build());
        try {
            pending.send();
        } catch (PendingIntent.CanceledException e) {
            e.printStackTrace();
        }
    }
}
