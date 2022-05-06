package com.abhishek.workoutapp.data.local

import android.content.Context
import android.content.res.Resources
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.abhishek.workoutapp.R
import com.abhishek.workoutapp.data.entity.Workout
import com.abhishek.workoutapp.data.local.dao.WorkoutDao
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch


@Database(version = 1, entities = [Workout::class], exportSchema = false)
abstract class WorkoutDatabase : RoomDatabase() {

    abstract fun workoutDao(): WorkoutDao

    private class WorkoutCollectionCallback(
        private val scope: CoroutineScope,
        private val resources: Resources
    ) : RoomDatabase.Callback() {

        override fun onCreate(db: SupportSQLiteDatabase) {
            super.onCreate(db)
            INSTANCE?.let { database ->
                scope.launch {
                    val workoutDao = database.workoutDao()
                    prePopulateDatabase(workoutDao)
                }
            }
        }

        private suspend fun prePopulateDatabase(workoutDao: WorkoutDao) {
            val jsonString = resources.openRawResource(R.raw.workout).bufferedReader().use {
                it.readText()
            }
            val typeToken = object : TypeToken<List<Workout>>() {}.type
            val workouts = Gson().fromJson<List<Workout>>(jsonString, typeToken)
            workoutDao.insertAllWorkouts(workout = workouts)
        }
    }

    companion object {

        @Volatile
        private var INSTANCE: WorkoutDatabase? = null

        fun getDatabase(
            context: Context,
            coroutineScope: CoroutineScope,
            resources: Resources
        ): WorkoutDatabase {
            val tempInstance = INSTANCE
            if (tempInstance != null) {
                return tempInstance
            }

            synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    WorkoutDatabase::class.java,
                    "players_database"
                )
                    .addCallback(WorkoutCollectionCallback(coroutineScope, resources))
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                return instance
            }
        }
    }
}