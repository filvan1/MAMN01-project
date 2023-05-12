package com.example.mamn01_project.ui.exercises;

import android.content.Intent;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.mamn01_project.FragmentEventListener;
import com.example.mamn01_project.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ExerciseFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ExerciseFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final String SUN_SALUTATION = "Solhälsning";
    private static final String BEACH_WALK = "Beachwalk";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private int mParam2;
    private FragmentEventListener listener;

    private Exercise exercise;
    /*Dessa variabler är temporära här för att testa solhälsning. Denna data ska komma från en
    separat class för varje övning sen
    private boolean overHead = false;
    private boolean toesTouched = false;
    private boolean exerciseFinished = false;


    private SensorManager sensorManager;
    private Sensor accelerometer;
    private Sensor gyroscope;
    private Sensor stepCounter;
*/

    public void setOnEventListener(FragmentEventListener l){
        this.listener = l;
    }


    public ExerciseFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ExerciseFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ExerciseFragment newInstance(String param1, int param2) {
        ExerciseFragment fragment = new ExerciseFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putInt(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getInt(ARG_PARAM2);
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(mParam2, container, false);
        /*if(mParam1 == null){
            Button button = view.findViewById(R.id.start_button);
            button.setOnClickListener(v -> {
                if(listener != null){
                    listener.onClick();
                }

            });
        } else{
            TextView field = view.findViewById(R.id.test);
            field.setText(mParam1);
        }*/
        Button button = view.findViewById(R.id.start_button);
        button.setOnClickListener(v -> {
            if(listener != null){
                listener.onClick();
            }

        });

        if(mParam1 != null){
            TextView field = view.findViewById(R.id.test);
            field.setText(mParam1);

        }

                // Inflate the layout for this fragment
        return view;
    }

    /**
     * Skapar ett exercise object baserat på namnet(string). Exercise objektet retureras
     * om namnet finns annars returerar null. Denna metod behövs i oncreate när vi tar emot
     * listan från wake, då måste vi konvertera listan av enabled exercise namn till Exercise objekt.
     * Det var mycket svårare att skicka objekten direkt därför blev denna lösning lättare.
     */
    private Exercise createExerciseByName(String exerciseName) {
        if (exerciseName.equals(BEACH_WALK)) {
            return new WalkStepsExercise(BEACH_WALK);
        } else if (exerciseName.equals(SUN_SALUTATION)) {
            return new SunSalutationExercise(SUN_SALUTATION);
        }

        return null;
    }

    public void Resume(){
        /*
        sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_NORMAL);
        sensorManager.registerListener(this, gyroscope, SensorManager.SENSOR_DELAY_NORMAL);
        sensorManager.registerListener(this, stepCounter, SensorManager.SENSOR_DELAY_NORMAL);*/
        exercise.Resume();
        Log.d("AlarmActivity", "onResume");
    }

    public void Pause(){
        //sensorManager.unregisterListener(this);
        exercise.Pause();
    }

    /**
     * onSensorChanged kallas när någon sensor får ett förändrat värde, så väldigt ofta.
     * */
    /*@Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        if (exercise != null && !exercise.isCompleted()) {

             * Den här koden är väldigt viktig. Istället för att implementera en sensorEventListener i
                    * varje subklass av exercise så kan vi istället använda metoden processSensorEvent i varje subclass
                    * på detta vis kan vi gå in i currentExercise (som är vår utvalde exercise när larmet går) och ändra
             * på värden. Exemelvis så kan vi ändra på currentSteps när sensorn i denna klassen känner att vi tar
             * ett steg. Nedsidan är att vi måste implementera processSensorEvent i varje subklass men uppsidan är
                    * mycket större då vi slipper ha en SensorEventListener i varje subklass och en mycket mer avancerad
                    * programstruktur
                    *
                    exercise.processSensorEvent(sensorEvent);
            /**
             * Vi kollar hela tiden om currentExercise är färdig.
             *
             if (exercise.isCompleted()) {
             listener.onClick();
             }
             }
             }
    */


}