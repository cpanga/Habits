package com.example.habits

import android.opengl.Visibility
import android.view.LayoutInflater
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.appcompat.content.res.AppCompatResources.getColorStateList
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.navigation.findNavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.habits.database.Habit
import com.example.habits.databinding.HabitListItemBinding
import com.example.habits.util.getStringFromDays
import java.util.logging.Logger

/**
 * Adapter for the [RecyclerView] in [HabitListFragment].
 */
class HabitListAdapter(private var hideFab: (() -> Unit)) :
    ListAdapter<Habit, HabitViewHolder>(DiffCallback) {
    companion object {
        val log: Logger = Logger.getLogger(HabitListAdapter::class.java.name)

        // Create DiffCallback object
        private val DiffCallback = object : DiffUtil.ItemCallback<Habit>() {
            override fun areItemsTheSame(oldItem: Habit, newItem: Habit): Boolean {
                return oldItem.uid == newItem.uid
            }

            override fun areContentsTheSame(oldItem: Habit, newItem: Habit): Boolean {
                return oldItem == newItem
            }
        }

    }

    // Inflate ViewHolder
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HabitViewHolder {
        return HabitViewHolder(
            HabitListItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    /**
     * Replaces the content of an existing view with new data
     */
    override fun onBindViewHolder(holder: HabitViewHolder, position: Int) {
        holder.bind(getItem(position), position, hideFab)
    }
}

// Create ViewHolder class
class HabitViewHolder(private var binding: HabitListItemBinding) :
    RecyclerView.ViewHolder(binding.root) {
    companion object {
        val log: Logger = Logger.getLogger(HabitViewHolder::class.java.name)
    }

    fun bind(habit: Habit, position: Int, hideFab: () -> Unit) {
        binding.recyclerHabitName.text = habit.habitName
        binding.recyclerHabitDays.text = getStringFromDays(habit.daysOfWeek, log)
        binding.recyclerHabitStreak.text = habit.streak.toString()
        binding.recyclerEdit.setOnClickListener {
            HabitListAdapter.log.info("USER: Clicked edit button on habit: ${habit.habitName} position: $position")
            hideFab()
            // TODO - Ideally would just pass in primary key, and avoid needing to parcelise the habit, just search for the item in the database instead.
            val action = HabitListFragmentDirections.actionFirstFragmentToSecondFragment(
                habit = habit,
                fragmentTitle = "Edit your Habit"
            )
            this.itemView.findNavController().navigate(action)
        }
        if (habit.notifActive == 1) {
            binding.recyclerCard.backgroundTintList =
                // TODO  - do dependency injection instead of this thing... (https://github.com/square/Dagger)
                getColorStateList(this.binding.recyclerEdit.context, R.color.md_theme_dark_error)
            binding.recyclerDone.visibility = VISIBLE
        } else {
            binding.recyclerCard.backgroundTintList =
                getColorStateList(this.binding.recyclerEdit.context, R.color.md_theme_dark_primary)
            binding.recyclerDone.visibility = GONE
        }
        binding.recyclerDone.setOnClickListener {
            log.info("habit done pressed for ${habit.habitName}")
        //    habitDone(habit)
        }
    }
}