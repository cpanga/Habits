package com.example.habits

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.activityViewModels
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.habits.database.Habit
import com.example.habits.util.getStringFromDays
import java.util.logging.Logger

/**
 * Adapter for the [RecyclerView] in [HabitListFragment].
 */
class HabitAdapter : RecyclerView.Adapter<HabitAdapter.HabitViewHolder>() {
    companion object {
        val log: Logger = Logger.getLogger(HabitAdapter::class.java.name)
    }

    // Create some placeholder objects for testing
    private val hab0 = Habit(1, "Habit 1", "Description", "1101011", 0, 22, 0)
    private val hab1 = Habit(1, "Habit 2", "Description", "1010011", 18, 45, 5)
    private val hab2 = Habit(1, "Habit 3", "Description", "1111111", 22, 0, 2)
    private val habits = listOf(hab0, hab1, hab2)

    // Create ViewHolder class
    class HabitViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val habitName: TextView = view.findViewById(R.id.recycler_habit_name)
        val daysOfWeek: TextView = view.findViewById(R.id.recycler_habit_days)
        val streak: TextView = view.findViewById(R.id.recycler_habit_streak)
        val edit: Button = view.findViewById(R.id.recycler_edit)
    }

    // Inflate ViewHolder
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HabitViewHolder {
        val layout = LayoutInflater
            .from(parent.context)
            .inflate(R.layout.habit_list_item, parent, false)
        return HabitViewHolder(layout)
    }

    /**
     * Replaces the content of an existing view with new data
     */
    override fun onBindViewHolder(holder: HabitViewHolder, position: Int) {
        val item = habits[position]
        holder.habitName.text = item.habitName
        holder.daysOfWeek.text = getStringFromDays(item.daysOfWeek, log)
        holder.streak.text = item.streak.toString()
        holder.edit.setOnClickListener {
            // TODO Hide FAB
            log.info("USER: Clicked edit button on habit: ${item.habitName}, position: $position")
            val action = HabitListFragmentDirections.actionFirstFragmentToSecondFragment(item)
            holder.itemView.findNavController().navigate(action)
        }
    }

    override fun getItemCount(): Int {
        return habits.size
    }
}
