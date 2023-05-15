package com.example.mamn01_project.ui.alarm;

import static android.content.Context.ALARM_SERVICE;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.transition.TransitionInflater;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.example.mamn01_project.R;
import com.example.mamn01_project.AlarmActivity;
import com.example.mamn01_project.R;
import com.example.mamn01_project.Wake;
import com.example.mamn01_project.databinding.FragmentAlarmBinding;

import java.util.Calendar;

public class AlarmFragment extends Fragment {
    private PendingIntent pending;
    private FragmentAlarmBinding binding;

    private AlarmManager alarmManager;

    private Intent intent;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        TransitionInflater transitionInflater = TransitionInflater.from(requireContext());
        setEnterTransition(transitionInflater.inflateTransition(R.transition.slide_right));
        setExitTransition(transitionInflater.inflateTransition(R.transition.slide_left));



    AlarmViewModel alarmViewModel =
                new ViewModelProvider(this).get(AlarmViewModel.class);


        binding = FragmentAlarmBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        final TextView textView = binding.textHome;

        // Need to call findViewById on the specific view that this fragment returns

        Button button = (Button) root.findViewById(R.id.alarm_button);

        // Alarm button
        button.setOnClickListener(new View.OnClickListener() {

            // Use Calendar to convert specific dates:hours:minutes to miliseconds.
            Calendar cal = Calendar.getInstance();
            int currentHour = cal.get(Calendar.HOUR_OF_DAY);
            int currentMinute = cal.get(Calendar.MINUTE);
            TextView alarmText = root.findViewById(R.id.text_home);

            TimePickerDialog pickerDialog = new TimePickerDialog(getActivity(), (timePicker, hours, minutes) ->{
                Calendar cal = Calendar.getInstance();
                // TODO: Make sure time picked is set to next date if required.
                cal.set(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH),cal.get(Calendar.DATE), hours, minutes, 0);

                long target= cal.getTimeInMillis();

                // Set up
                alarmManager = (AlarmManager) getActivity().getSystemService(ALARM_SERVICE);
                // Set up broadcast so that the onReceive method of Receiver is invoked at specified time
                intent = new Intent(getResources().getString(R.string.intent_alarm_trigger));
                //intent.setAction(getResources().getString(R.string.intent_alarm_trigger));
                pending = PendingIntent.getBroadcast(getActivity(), 234324243, intent, PendingIntent.FLAG_IMMUTABLE);

                alarmManager.setAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, target, pending);

                Toast.makeText(getActivity(), "Alarm set for " + hours + ":" + minutes + ".", Toast.LENGTH_LONG).show();

                if (minutes >= 0 && minutes <= 9) {
                    alarmText.setText(hours + ":0" + minutes);
                } else {
                    alarmText.setText(hours + ":" + minutes);
                }
            }, currentHour, currentMinute, true);



            public void onClick(View v) {
                Log.d("BUTTONS", "User tapped the button");
                pickerDialog.show();
            }
        });

        return root;
    }

    private void stopAlarm() {
        if (alarmManager != null && pending != null) {
            alarmManager.cancel(pending);
            Toast.makeText(getActivity(), "Alarm stopped.", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}