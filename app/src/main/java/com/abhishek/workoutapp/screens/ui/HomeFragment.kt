package com.abhishek.workoutapp.screens.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.children
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.abhishek.workoutapp.R
import com.abhishek.workoutapp.data.entity.Workout
import com.abhishek.workoutapp.databinding.FragmentHomeBinding
import com.abhishek.workoutapp.screens.viewmodel.WorkoutViewModel
import com.google.android.material.chip.Chip

class HomeFragment : Fragment() {
    private lateinit var _binding: FragmentHomeBinding
    private val binding get() = _binding
    private lateinit var workoutViewModel: WorkoutViewModel
    private lateinit var workoutAdapter : WorkoutAdapter



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        // Inflate the layout for this fragment
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        activity.let {
            workoutViewModel = ViewModelProvider(it!!).get(WorkoutViewModel::class.java)
        }
        workoutAdapter = WorkoutAdapter(mutableListOf())
        workoutAdapter.setOnWorkoutListener { workout->
            workoutViewModel.setSelectedWorkout(workout)
            findNavController().navigate(R.id.exerciseDetailsFragment,
                arguments,
                NavOptions.Builder().setPopUpTo(R.id.exerciseDetailsFragment,
                    true).build())
        }

        withTags()
        attachObservers()
    }

    private fun attachObservers() {
        workoutViewModel.workouts.observe(viewLifecycleOwner) {
            binding.rvExercises.apply {
                adapter = workoutAdapter
                layoutManager = LinearLayoutManager(activity)
            }
            workoutAdapter.showData(it)
        }
    }


    private fun withTags() {
        binding.chipGroupFilter.children.forEach {
            val chip  = (it as Chip)
            chip.setOnCheckedChangeListener { _, b ->
                if (b) {
                    workoutViewModel.difficulty.value = chip.tag as String
                }
            }
        }
    }
}