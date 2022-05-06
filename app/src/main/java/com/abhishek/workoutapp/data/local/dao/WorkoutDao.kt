package com.abhishek.workoutapp.data.local.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.abhishek.workoutapp.data.entity.Workout
import kotlinx.coroutines.flow.Flow

@Dao
interface WorkoutDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllWorkouts(workout : List<Workout>)

    @Query("SELECT * FROM workout")
    fun getWorkouts() : LiveData<List<Workout>>

    @Query(value = "SELECT * FROM workout")
    fun getAllWorkouts() : Flow<List<Workout>>

    @Query(value = "SELECT * FROM workout")
    fun getWorkoutList() : List<Workout>

    @Query(value = "SELECT * FROM workout WHERE difficultyLevel=:difficulty")
    fun getOnBasisOfDifficulty(difficulty : String?) : LiveData<List<Workout>>

    @Query(value = "SELECT * FROM workout WHERE difficultyLevel=:difficulty")
    fun getByBasisOfDifficulty(difficulty : String?) : Flow<List<Workout>>

}
