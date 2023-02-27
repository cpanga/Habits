package com.example.habits

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.navigation.Navigation.findNavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.recyclerview.widget.RecyclerView
import com.example.habits.database.Habit
import com.example.habits.util.toBundle

/**
 * Adapter for the [RecyclerView] in [FirstFragment].
 */
class HabitAdapter : RecyclerView.Adapter<HabitAdapter.HabitViewHolder>() {

    private val hab0 = Habit(1,"Habit 1", "description", listOf(true,true,false,true,true,true,true),9,0,0)
    private val hab1 = Habit(1,"Habit 2", "description", listOf(false,true,true,false,true,true,true),9,0,5)
    private val hab2 = Habit(1,"Habit 3", "description", listOf(true,false,true,true,true,false,false),9,0,2)

    private val habits = listOf(hab0,hab1,hab2)

    class HabitViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val habitName: TextView = view.findViewById(R.id.recycler_habit_name)
        val daysOfWeek: TextView = view.findViewById(R.id.recycler_habit_days)
        val streak: TextView = view.findViewById(R.id.recycler_habit_streak)
        val edit: Button = view.findViewById(R.id.recycler_edit)
    }

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
        holder.daysOfWeek.text = getStringFromDays(item.daysOfWeek)
        holder.streak.text = item.streak.toString()
        holder.edit.setOnClickListener {
            val action = FirstFragmentDirections.actionFirstFragmentToSecondFragment(item.toBundle(),true)
            holder.itemView.findNavController().navigate(action)
        }
    }

    override fun getItemCount(): Int {
        return habits.size
    }

    private fun getStringFromDays(days: List<Boolean>): String {

        var str = ""

        if (days[0]) str += "Mon "
        if (days[1]) str += "Tue "
        if (days[2]) str += "Wed "
        if (days[3]) str += "Thu "
        if (days[4]) str += "Fri "
        if (days[5]) str += "Sat "
        if (days[6]) str += "Sun "

        return str
    }
}
