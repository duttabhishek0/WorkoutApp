package com.abhishek.workoutapp.data.repository

import androidx.lifecycle.LiveData
import com.abhishek.workoutapp.data.entity.Workout
import com.abhishek.workoutapp.data.local.dao.WorkoutDao
import kotlinx.coroutines.flow.Flow

class WorkoutRepository(private val exerciseDao: WorkoutDao) {

    fun getAllWorkouts(): LiveData<List<Workout>> {
        return exerciseDao.getWorkouts()
    }

    fun getWorkoutsByDifficulty(difficulty: String): LiveData<List<Workout>> {
        return exerciseDao.getOnBasisOfDifficulty(difficulty)
    }

    fun getAllOrSearch(difficulty: String): Flow<List<Workout>> {
        return if (difficulty == "all") {
            exerciseDao.getAllWorkouts()
        } else {
            exerciseDao.getByBasisOfDifficulty(difficulty)
        }
    }
}