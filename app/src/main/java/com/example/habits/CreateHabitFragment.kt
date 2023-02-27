package com.example.habits

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.habits.databinding.FragmentSecondBinding
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
import java.util.logging.Logger

/**
 * A simple [Fragment] subclass as the second destination in the navigation.
 */
class CreateHabitFragment : Fragment() {
    companion object {
        val log: Logger = Logger.getLogger(CreateHabitFragment::class.java.name)
    }

    // State of each day of week button
    private val monEnabled = MutableLiveData<Boolean>().apply {postValue(false)}
    private val tueEnabled = MutableLiveData<Boolean>().apply {postValue(false)}
    private val wedEnabled = MutableLiveData<Boolean>().apply {postValue(false)}
    private val thuEnabled = MutableLiveData<Boolean>().apply {postValue(false)}
    private val friEnabled = MutableLiveData<Boolean>().apply {postValue(false)}
    private val satEnabled = MutableLiveData<Boolean>().apply {postValue(false)}
    private val sunEnabled = MutableLiveData<Boolean>().apply {postValue(false)}

    private val reminderTime = MutableLiveData<List<Int>>().apply { postValue(listOf(9,0)) }
    private var streak:Int = 0
    private val habitName = MutableLiveData<String>()
    private val habitDesc = MutableLiveData<String>()

    private var _binding: FragmentSecondBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    private val viewModel: MainActivityViewModel by activityViewModels()
    private var textBoxesInteractedWith: Boolean = false


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSecondBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        populateValues()
        //updateHabitNameAndDesc()

        binding.buttonSecond.setOnClickListener {
            log.info("USER: Save button pressed")

            if (!textBoxesInteractedWith) {
                log.info("Text boxes have not been interacted with. Flag errors")
                val habitNameEmpty = binding.habitName.editText?.text?.trim()?.isEmpty() == true
                val habitDescEmpty = binding.habitDesc.editText?.text?.trim()?.isEmpty() == true
                showTextBoxError(
                    habitNameEmpty,
                    binding.habitName,
                    getString(R.string.habit_name_error)
                )
                showTextBoxError(
                    habitDescEmpty,
                    binding.habitDesc,
                    getString(R.string.habit_desc_error)
                )

            } else if ( (binding.habitName.error != null) || (binding.habitDesc.error != null)) {
                log.info("Errors in text boxes, not proceeding with save")
                return@setOnClickListener
            } else {
                if (viewModel.moreThanZeroDaysSelected(
                        monEnabled,
                        tueEnabled,
                        wedEnabled,
                        thuEnabled,
                        friEnabled,
                        satEnabled,
                        sunEnabled
                )) {
                    log.info("Saving habit")
                    findNavController().navigate(R.id.action_SecondFragment_to_FirstFragment)
                    applyDefaultValues()
                } else {
                    log.info("No days selected. Show snackbar")
                    createNoDaysSelectedSnackbar()
                }
            }
        }

        log.info("Set Observers and Listeners")
        setOnTextChangedListeners(binding.habitName, getString(R.string.habit_name_error))
        setOnTextChangedListeners(binding.habitDesc, getString(R.string.habit_desc_error))
        setDayOfWeekObserversAndListeners()
        setTextBoxObservers(binding.reminderTimeBox, reminderTime)
        setTextBoxListener(binding.reminderTimeBox)

    }

    private fun createNoDaysSelectedSnackbar() {
        Snackbar.make(requireActivity().findViewById(R.id.editable),"Please select at least one day of the week", Snackbar.LENGTH_LONG).show()
    }
    
    private fun setOnTextChangedListeners(textBox: TextInputLayout, errorText: String) {
        textBox.editText?.doAfterTextChanged {
            showTextBoxError(it?.trim()?.isEmpty() == true, textBox, errorText)
            textBoxesInteractedWith = true
        }
    }

    private fun showTextBoxError(shouldShow: Boolean, textBox: TextInputLayout, errorText: String) {
        if (shouldShow) {
            textBox.error = errorText
        } else {
            textBox.error = null
        }
    }

    private fun setTextBoxListener(textBox: TextInputEditText) {
        textBox.setOnClickListener {
            log.info("User: Pressed Time textbox")
            val materialTimePicker: MaterialTimePicker = MaterialTimePicker.Builder()
                .setTitleText("Select reminder time")
                .setHour(reminderTime.value?.first() ?: 9)
                .setMinute(reminderTime.value?.last() ?: 0)
                .setTimeFormat(TimeFormat.CLOCK_12H)
                .build()

            materialTimePicker.apply {
                show(this@CreateHabitFragment.parentFragmentManager,"MainActivity")
                addOnPositiveButtonClickListener {
                    reminderTime.postValue(listOf(this.hour,this.minute))
                }
            }
        }
    }

    private fun setTextBoxObservers(textBox: TextInputEditText, reminderTime: MutableLiveData<List<Int>>) {
        val textBoxObserver = Observer<List<Int>> {
            textBox.setText(viewModel.convertTimeToString(it.first(),it.last()))
        }
        reminderTime.observe(viewLifecycleOwner, textBoxObserver)

    }

    private fun setDayOfWeekObserversAndListeners() {
        setDayOfWeekObserversAndListenerDay(binding.mon, monEnabled)
        setDayOfWeekObserversAndListenerDay(binding.tue, tueEnabled)
        setDayOfWeekObserversAndListenerDay(binding.wed, wedEnabled)
        setDayOfWeekObserversAndListenerDay(binding.thu, thuEnabled)
        setDayOfWeekObserversAndListenerDay(binding.fri, friEnabled)
        setDayOfWeekObserversAndListenerDay(binding.sat, satEnabled)
        setDayOfWeekObserversAndListenerDay(binding.sun, sunEnabled)
    }

    private fun setDayOfWeekObserversAndListenerDay(button: Button, enabled: MutableLiveData<Boolean>) {
        val fabObserver = Observer<Boolean> {
            if (enabled.value != false) button.apply{
                alpha = 1.0F
            }
            else button.alpha = 0.5F
        }
        enabled.observe(viewLifecycleOwner, fabObserver)

        button.setOnClickListener{
            enabled.postValue(!(enabled.value ?: false) )
        }
    }

    private fun populateValues() {
        val args: CreateHabitFragmentArgs by navArgs()
        val habit = args.habit
        if (habit == null) applyDefaultValues()
        else {
            log.info("Applying saved values. habit name = ${habit.habitName}")
            habitName.postValue(habit.habitName)
            habitDesc.postValue(habit.habitDesc)
            // TODO - fix hack here
            binding.habitName.editText?.setText(habit.habitName)
            binding.habitDesc.editText?.setText(habit.habitDesc)
            streak = habit.streak
            reminderTime.postValue(listOf(habit.notifHour, habit.notifMin))
            viewModel.postDayValueFromString(
                monEnabled,
                tueEnabled,
                wedEnabled,
                thuEnabled,
                friEnabled,
                satEnabled,
                sunEnabled,
                daysString = habit.daysOfWeek
            )
        }
    }

    // TODO - handle this better?
    private fun updateHabitNameAndDesc() {
        log.info("Updating Habit Name and Description boxes. Name:${habitName.value}, Desc:${habitDesc.value}")
        binding.habitName.editText?.setText(habitName.value)
        binding.habitDesc.editText?.setText(habitDesc.value)
    }

    private fun applyDefaultValues() {
        // Reset the new habit form values to the default
        viewModel.postDayValueFalse(
            monEnabled,
            tueEnabled,
            wedEnabled,
            thuEnabled,
            friEnabled,
            satEnabled,
            sunEnabled
        )
        reminderTime.postValue((listOf(9,0)))
        habitDesc.postValue("")
        habitName.postValue("")
        streak = 0
    }

    override fun onDestroyView() {
        super.onDestroyView()
        viewModel.fabVisible.postValue(true)
        _binding = null
    }
}