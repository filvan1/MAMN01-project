package com.example.mamn01_project.ui.alarm;


import android.util.Log;

import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;


public class AlarmViewModel extends ViewModel {
    public final MutableLiveData <String> alarmString;
    public static final String ALARM_NOT_SET = "- -:- -";

    public AlarmViewModel() {
        alarmString = new MutableLiveData<>(ALARM_NOT_SET);
    }

    public LiveData<String> getAlarm(){
        Log.d("AlarmGet", "GET ALARM RETURNED "+ alarmString.getValue());
        return alarmString;
    }

    public void setAlarmString(String text){
        alarmString.setValue(text);
    }


}