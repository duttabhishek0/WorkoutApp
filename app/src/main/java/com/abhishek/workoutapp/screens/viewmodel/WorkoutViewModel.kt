package com.abhishek.workoutapp.screens.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.abhishek.workoutapp.data.entity.Workout
import com.abhishek.workoutapp.data.local.WorkoutDatabase
import com.abhishek.workoutapp.data.repository.WorkoutRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flatMapLatest

@OptIn(ExperimentalCoroutinesApi::class)
class WorkoutViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: WorkoutRepository
    val difficulty = MutableStateFlow("all")
    val selectedWorkout = MutableLiveData<Workout>()

    init {
        val workoutDao = WorkoutDatabase
            .getDatabase(application, viewModelScope, application.resources)
            .workoutDao()
        repository = WorkoutRepository(workoutDao)
    }

    fun setSelectedWorkout(workout: Workout) {
        selectedWorkout.value = workout
    }

    fun getAllWorkouts(): LiveData<List<Workout>> {
        return repository.getAllWorkouts()
    }

    val workouts = difficulty.flatMapLatest {
        repository.getAllOrSearch(it)
    }.asLiveData()

    fun getWorkoutByDifficulty(difficulty: String): LiveData<List<Workout>> {
        return repository.getWorkoutsByDifficulty(difficulty)
    }
}
