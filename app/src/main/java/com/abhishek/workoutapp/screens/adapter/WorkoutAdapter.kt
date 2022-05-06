package com.abhishek.workoutapp.screens.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.abhishek.workoutapp.R
import com.abhishek.workoutapp.data.entity.Workout
import com.abhishek.workoutapp.databinding.ItemIndividualWorkoutBinding
import com.bumptech.glide.Glide

class WorkoutAdapter(
    private val workouts : MutableList<Workout>
) : RecyclerView.Adapter<WorkoutAdapter.WorkoutVH>(){
    private var listener : ((Workout) -> Unit) ?= null
    private lateinit var binding : ItemIndividualWorkoutBinding

    inner class WorkoutVH(itemView :ItemIndividualWorkoutBinding ) : RecyclerView.ViewHolder(itemView.root)
    fun showData(workouts: List<Workout>){
        this.workouts.clear()
        this.workouts.addAll(workouts)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WorkoutVH {
        binding = ItemIndividualWorkoutBinding.inflate(LayoutInflater.from(parent.context), parent,false )
        return WorkoutVH(binding)
    }

    override fun onBindViewHolder(holder: WorkoutVH, position: Int) {
        val item = workouts[position]
        holder.itemView.apply {
            binding.apply {
                exerciseName.text = item.exerciseName
                difficultyLevel.text = item.difficultyLevel
                ("primary: " + item.primaryMuscles).also { primaryMuscles.text = it }
                ("secondary: " + item.secondaryMuscles).also { secondaryMuscles.text = it }
                requiredEquipment.text = item.requiredEquipment

                Glide.with(mainExerciseImage.context)
                    .load(item.image)
                    .centerInside()
                    .placeholder(R.drawable.image_three)
                    .into(mainExerciseImage)
            }
            holder.itemView.setOnClickListener {
                listener?.invoke(workouts[position])
            }
        }
    }
    fun setOnWorkoutListener(listener: ((Workout) -> Unit)) {
        this.listener = listener
    }
    override fun getItemCount(): Int {
        return workouts.size
    }
}