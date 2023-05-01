package com.example.mamn01_project.ui.exercises;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mamn01_project.databinding.FragmentExercisesBinding;

import java.util.ArrayList;

public class ExercisesFragment extends Fragment {

    private FragmentExercisesBinding binding;
    private ExerciseAdapter adapter;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        ExercisesViewModel exercisesViewModel =
                new ViewModelProvider(this).get(ExercisesViewModel.class);

        binding = FragmentExercisesBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        RecyclerView recyclerView = binding.recyclerView;
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        adapter = new ExerciseAdapter(new ArrayList<>());
        recyclerView.setAdapter(adapter);

        exercisesViewModel.getExercises().observe(getViewLifecycleOwner(), exercises -> {
            adapter.setExercises(exercises);
            adapter.notifyDataSetChanged();
        });
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}