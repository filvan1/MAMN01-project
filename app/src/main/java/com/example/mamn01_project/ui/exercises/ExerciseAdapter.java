package com.example.mamn01_project.ui.exercises;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.ToggleButton;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mamn01_project.R;

import java.util.List;

public class ExerciseAdapter extends RecyclerView.Adapter<ExerciseAdapter.ViewHolder> {

    private List<Exercise> exerciseList;
    private ExercisesViewModel exercisesViewModel;
    public ExerciseAdapter(List<Exercise> exerciseList, ExercisesViewModel exercisesViewModel) {
        this.exerciseList = exerciseList;
        this.exercisesViewModel = exercisesViewModel;
    }

    @NonNull
    @Override
    public ExerciseAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.exercise_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ExerciseAdapter.ViewHolder holder, int position) {
        Exercise exercise = exerciseList.get(position);
        holder.exerciseName.setText(exercise.getName());
        holder.toggleButton.setChecked(exercise.isEnabled());

        holder.toggleButton.setOnCheckedChangeListener((buttonView, isChecked) -> {
            exercise.setEnabled(isChecked);
            List<Exercise> enabledExercises = exercisesViewModel.getEnabledExercises().getValue();
            if (isChecked && !enabledExercises.contains(exercise)) {
                enabledExercises.add(exercise);
            } else if (!isChecked) {
                enabledExercises.remove(exercise);
            }
            exercisesViewModel.setEnabledExercises(enabledExercises);
        });


    }

    @Override
    public int getItemCount() {
        return exerciseList.size();
    }

    public void setExercises(List<Exercise> exercises) {
        this.exerciseList = exercises;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView exerciseName;
        ToggleButton toggleButton;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            exerciseName = itemView.findViewById(R.id.exercise_name);
            toggleButton = itemView.findViewById(R.id.toggle_button);
        }
    }
}
