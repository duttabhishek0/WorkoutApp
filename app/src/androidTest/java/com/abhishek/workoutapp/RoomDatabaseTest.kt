package com.abhishek.workoutapp

import android.content.Context
import androidx.room.Room
import androidx.room.withTransaction
import androidx.test.espresso.matcher.ViewMatchers.assertThat
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.platform.app.InstrumentationRegistry
import com.abhishek.workoutapp.data.entity.Workout
import com.abhishek.workoutapp.data.local.WorkoutDatabase
import com.abhishek.workoutapp.screens.ui.MainActivity
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.runBlocking
import org.hamcrest.CoreMatchers
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.io.InputStream

class RoomDatabaseTest {
    @get:Rule
    var activityScenarioRule = ActivityScenarioRule(MainActivity::class.java)
    private lateinit var context: Context
    private lateinit var db: WorkoutDatabase
    private lateinit var workouts: List<Workout>

    @Before
    fun setUp() {

        context = InstrumentationRegistry.getInstrumentation().targetContext
        db = Room.inMemoryDatabaseBuilder(context, WorkoutDatabase::class.java).build()

        val jsonStream: InputStream = context.resources.openRawResource(R.raw.workout)
        val jsonBytes: ByteArray = jsonStream.readBytes()

        val typeToken = object : TypeToken<List<Workout>>() {}.type
        workouts = Gson().fromJson(String(jsonBytes), typeToken)

        runBlocking {
            db.withTransaction {
                db.workoutDao().insertAllWorkouts(workout = workouts)
            }
        }
    }

    @Test
    fun get_exercise_size_returns_4() {
        runBlocking {
            assertThat(db.workoutDao().getWorkoutList().size, CoreMatchers.equalTo(6))
        }
    }

    @After
    fun clear() {
        db.clearAllTables()
        db.close()
    }
}
