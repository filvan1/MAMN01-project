package com.example.mamn01_project.ui.exercises;

import android.content.Context;
import android.graphics.Color;
import android.hardware.SensorManager;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.os.Vibrator;
import android.os.VibratorManager;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
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
    private static final String ARG_PARAM3 = "param3";
    private static final String SUN_SALUTATION = "Sun salutation";
    private static final String BEACH_WALK = "Beachwalk";

    // TODO: Rename and change types of parameters
    private String exerciseName;
    private int mParam2;
    private FragmentEventListener listener;
    private SensorManager sensorManager;
    private Vibrator vibrator;
    private boolean isRotated;
    private int numExercises = 0;

    private Exercise exercise;

    public static ExerciseFragment newInstance(String exerciseName, int fragment_alert, int numExercises) {
        ExerciseFragment fragment = new ExerciseFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, exerciseName);
        args.putInt(ARG_PARAM2, fragment_alert);
        args.putInt(ARG_PARAM3, numExercises);
        fragment.setArguments(args);
        return fragment;
    }
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
     * @param exerciseName Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ExerciseFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ExerciseFragment newInstance(String exerciseName, int param2) {
        ExerciseFragment fragment = new ExerciseFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, exerciseName);
        args.putInt(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            exerciseName = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getInt(ARG_PARAM2);
            numExercises = getArguments().getInt(ARG_PARAM3);
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(mParam2, container, false);
        if(exerciseName == null){
            Button button = view.findViewById(R.id.start_button);
            if(numExercises == 0){
                button.setText("Turn off alarm");
            } else {
                button.setBackgroundColor(getResources().getColor(R.color.nw_primary, getActivity().getTheme()));
            }
            button.setOnClickListener(v -> {
                if(listener != null){
                    listener.onEvent();
                }

            });
        } else{
        }


        if(exerciseName != null){
            Context context = getContext();

            sensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);

            vibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);

            TextView exerciseNameText = view.findViewById(R.id.exercise_name);
            TextView repsLeftText = view.findViewById(R.id.reps_left);
            ImageView imageView = view.findViewById(R.id.image_view);
            TextView textRep = view.findViewById(R.id.text_rep);

            TextView exerciseNameTextRot = view.findViewById(R.id.exercise_name_rotated);
            TextView textRepRot = view.findViewById(R.id.text_rep_rotated);

            exerciseNameText.setText(exerciseName.toUpperCase());

            switch(exerciseName) {

                case "Sun salutation":
                    imageView.setImageResource(R.drawable.sun_salutation);
                    Glide.with(this).asGif().load(R.drawable.sun_salutation).into(imageView);

                    if(!isRotated) {
                        repsLeftText.setRotation(90);
                        imageView.setRotation(90);
                        isRotated = true;


                    }

                    exerciseNameTextRot.setVisibility(View.VISIBLE);
                    textRepRot.setVisibility(View.VISIBLE);
                    exerciseNameText.setVisibility(View.INVISIBLE);
                    textRep.setVisibility(View.INVISIBLE);

                    //textRep.setText("REPS LEFT");
                    repsLeftText.setVisibility(View.VISIBLE);
                    repsLeftText.setTextColor(Color.WHITE);

                    exercise = new SunSalutationExercise(exerciseName, sensorManager, repsLeftText, listener, vibrator);
                    break;
                case "Beachwalk":
                    imageView.setImageResource(R.drawable.beach_walk);
                    Glide.with(this).asGif().load(R.drawable.beach_walk).into(imageView);

                    if(isRotated) {
                        repsLeftText.setRotation(-90);
                        imageView.setRotation(-90);
                        isRotated = false;
                    }

                    exerciseNameTextRot.setVisibility(View.INVISIBLE);
                    textRepRot.setVisibility(View.INVISIBLE);
                    exerciseNameText.setVisibility(View.VISIBLE);
                    textRep.setVisibility(View.VISIBLE);


                    textRep.setTextColor(Color.WHITE);
                    textRep.setText("REPS LEFT");
                    repsLeftText.setVisibility(View.VISIBLE);
                    repsLeftText.setTextColor(Color.WHITE);

                    exercise = new WalkStepsExercise(exerciseName, sensorManager, repsLeftText, listener, vibrator);
                    break;
                case "Sunlight":
                    imageView.setImageResource(R.drawable.sunlight);
                    Glide.with(this).asGif().load(R.drawable.sunlight).into(imageView);

                    if(isRotated) {
                        repsLeftText.setRotation(-90);
                        imageView.setRotation(-90);
                        isRotated = false;
                    }

                    exerciseNameTextRot.setVisibility(View.INVISIBLE);
                    textRepRot.setVisibility(View.INVISIBLE);
                    exerciseNameText.setVisibility(View.VISIBLE);
                    textRep.setVisibility(View.VISIBLE);

                    textRep.setTextColor(Color.WHITE);
                    textRep.setText("LET THERE BE LIGHT");

                    repsLeftText.setVisibility(View.INVISIBLE);


                    exercise = new LightExercise(exerciseName, sensorManager,textRep, listener, vibrator);
                    break;
            }



        }

                // Inflate the layout for this fragment
        return view;
    }

    public void Resume(){
        /*
        sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_NORMAL);
        sensorManager.registerListener(this, gyroscope, SensorManager.SENSOR_DELAY_NORMAL);
        sensorManager.registerListener(this, stepCounter, SensorManager.SENSOR_DELAY_NORMAL);*/
        if(exercise != null) exercise.Resume();
        Log.d("AlarmActivity", "onResume");
    }

    public void Pause(){
        //sensorManager.unregisterListener(this);
        if(exercise != null) exercise.Pause();
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