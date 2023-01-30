package com.example.habits

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.example.habits.databinding.FragmentSecondBinding
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
        val log = Logger.getLogger(CreateHabitFragment::class.java.name)
    }

    private var _binding: FragmentSecondBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    private val viewModel: MainActivityViewModel by activityViewModels()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSecondBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.buttonSecond.setOnClickListener {
            findNavController().navigate(R.id.action_SecondFragment_to_FirstFragment)
        }

        log.info("Set Observers and Listeners")
        setDayOfWeekObserversAndListeners()
        setTextBoxObservers(binding.reminderTimeBox, viewModel.reminderTime)
        setTextBoxListener(binding.reminderTimeBox)

    }

    private fun setTextBoxListener(textBox: TextInputEditText) {

        textBox.setOnClickListener {
            log.info("User: Pressed Time textbox")
            val materialTimePicker: MaterialTimePicker = MaterialTimePicker.Builder()
                .setTitleText("Select reminder time")
                .setHour(viewModel.reminderTime.value?.first() ?: 9)
                .setMinute(viewModel.reminderTime.value?.last() ?: 0)
                .setTimeFormat(TimeFormat.CLOCK_12H)
                .build()

            materialTimePicker.apply {
                show(this@CreateHabitFragment.parentFragmentManager,"MainActivity")
                addOnPositiveButtonClickListener {
                    viewModel.reminderTime.postValue(listOf(this.hour,this.minute))
                }
            }
        }
    }

    private fun setTextBoxObservers(textBox: TextInputEditText, reminderTime: MutableLiveData<List<Int>>) {
        val textBoxObserver = Observer<List<Int>> {
            textBox.setText(convertTimeToString(it.first(),it.last()))
        }
        reminderTime.observe(viewLifecycleOwner, textBoxObserver)

    }

    private fun convertTimeToString(hour: Int, minute:Int): String {
        val amOrPm = if (hour>11) "PM" else "AM"
        val adjHour = if (hour>12) hour-12 else hour
        val adjMin = if (minute<10) "0$minute" else minute

        return "$adjHour:$adjMin $amOrPm"
    }
    private fun setDayOfWeekObserversAndListeners() {
        setDayOfWeekObserversAndListenerDay(binding.mon, viewModel.monEnabled)
        setDayOfWeekObserversAndListenerDay(binding.tue, viewModel.tueEnabled)
        setDayOfWeekObserversAndListenerDay(binding.wed, viewModel.wedEnabled)
        setDayOfWeekObserversAndListenerDay(binding.thu, viewModel.thuEnabled)
        setDayOfWeekObserversAndListenerDay(binding.fri, viewModel.friEnabled)
        setDayOfWeekObserversAndListenerDay(binding.sat, viewModel.satEnabled)
        setDayOfWeekObserversAndListenerDay(binding.sun, viewModel.sunEnabled)
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

    override fun onDestroyView() {
        super.onDestroyView()
        viewModel.fabVisibile.postValue(true)
        _binding = null
    }
}