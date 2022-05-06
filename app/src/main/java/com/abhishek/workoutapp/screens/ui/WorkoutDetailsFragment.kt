package com.abhishek.workoutapp.screens.ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.abhishek.workoutapp.R
import com.abhishek.workoutapp.databinding.FragmentWorktoutDetailsBinding
import com.abhishek.workoutapp.screens.viewmodel.WorkoutViewModel
import com.bumptech.glide.Glide

class WorkoutDetailsFragment : Fragment() {
    private lateinit var _binding: FragmentWorktoutDetailsBinding
    private val binding get() = _binding
    private lateinit var workoutViewModel: WorkoutViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentWorktoutDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        activity.let {
            workoutViewModel = ViewModelProvider(it!!)
                .get(WorkoutViewModel::class.java)
        }

        val onBackPressedCallback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                findNavController().navigate(
                    R.id.action_exerciseDetailsFragment_to_homeFragment
                )
            }
        }

        requireActivity()
            .onBackPressedDispatcher
            .addCallback(
                viewLifecycleOwner,
                onBackPressedCallback
            )

        workoutViewModel.selectedWorkout.observe(
            viewLifecycleOwner
        ) {
            val imageUrl = it.image
            Log.d("TAG", "onViewCreatedDetail:$it ")
            binding.apply {
                exerciseName.text = it.exerciseName
                difficultyLevelTv.text = it.difficultyLevel
                requiredEquipmentTv.text = it.requiredEquipment
                primaryMusclesTv.text = it.primaryMuscles
                secondaryMusclesTv.text = it.secondaryMuscles
                descriptionTv.text = it.description
                fav.setOnClickListener {
                    Toast.makeText(
                        context,
                        "Added to favourites",
                        Toast.LENGTH_SHORT
                    ).show()
                }

                Glide.with(exerciseImage.context)
                    .load(imageUrl)
                    .placeholder(R.drawable.image_three)
                    .error(R.drawable.primary)
                    .fitCenter()
                    .centerCrop()
                    .into(exerciseImage)
            }
        }
    }
}
