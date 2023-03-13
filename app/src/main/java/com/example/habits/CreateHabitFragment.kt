package com.example.habits

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.habits.databinding.FragmentHabitCreateBinding
import com.example.habits.util.convertTimeToString
import com.example.habits.util.ensureAtLeastOneIsTrue
import com.example.habits.util.postDayValueFromString
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

    private lateinit var uiModel: CreateHabitFragmentUiModel
    private var _binding: FragmentHabitCreateBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    private val viewModel: MainActivityViewModel by activityViewModels()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHabitCreateBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // Define uiModel as ViewModel's uiModel
        uiModel = viewModel.createHabitUiModel

        populateValues()
        log.info("name: ${uiModel.nameTextInteractedWith.value}, desc: ${uiModel.descTextInteractedWith.value}")

        binding.createButton.setOnClickListener {
            log.info("USER: ${binding.createButton.text} button pressed")

            // If there are errors with the text box inputs, log and cancel saving
            if ((binding.habitName.error != null) || (binding.habitDesc.error != null)) {
                log.info("Errors in text boxes, not proceeding with save")
                return@setOnClickListener
            }

            // Check that at least one day is selected. If not, show error snackbar
            if (!ensureAtLeastOneIsTrue(
                    uiModel.monEnabled,
                    uiModel.tueEnabled,
                    uiModel.wedEnabled,
                    uiModel.thuEnabled,
                    uiModel.friEnabled,
                    uiModel.satEnabled,
                    uiModel.sunEnabled
                )
            ) {
                log.info("No days selected. Show snackbar")
                createNoDaysSelectedSnackbar()
            } else {
                log.info("Saving habit")
                // TODO save habit
                findNavController().navigate(R.id.action_SecondFragment_to_FirstFragment)
            }
        }

        // Set UI observers and listeners
        log.info("Set Observers and Listeners")
        log.info("is interacted with? name:${uiModel.nameTextInteractedWith.value}, desc:${uiModel.descTextInteractedWith.value}")
        setOnTextChangedListeners(
            binding.habitName,
            getString(R.string.habit_name_error),
            uiModel.nameTextInteractedWith
        )
        setOnTextChangedListeners(
            binding.habitDesc,
            getString(R.string.habit_desc_error),
            uiModel.descTextInteractedWith
        )
        setDayOfWeekObserversAndListeners()
        setReminderTimeObserver(binding.reminderTimeBox, uiModel.reminderTime)
        setTextBoxObserver(binding.habitName, uiModel.habitName)
        setTextBoxObserver(binding.habitDesc, uiModel.habitDesc)
        setTimePickerBoxListener(binding.reminderTimeBox)

    }

    private fun createNoDaysSelectedSnackbar() {
        Snackbar.make(
            requireActivity().findViewById(R.id.editable),
            getString(R.string.select_at_least_one_day),
            Snackbar.LENGTH_LONG
        ).show()
    }

    private fun showTextBoxError(
        shouldShow: Boolean,
        textBoxInteractedWith: Boolean,
        textBox: TextInputLayout,
        errorText: String
    ) {
        if (shouldShow && textBoxInteractedWith) {
            log.info("Show error for $textBox: $errorText")
            textBox.error = errorText
        } else {
            textBox.error = null
        }
    }

    private fun setTimePickerBoxListener(textBox: TextInputEditText) {
        textBox.setOnClickListener {
            log.info("User: Pressed Time text box to open timepicker")
            val materialTimePicker: MaterialTimePicker =
                MaterialTimePicker.Builder().setTitleText(getString(R.string.select_reminder_time))
                    .setHour(uiModel.reminderTime.value?.first() ?: 9)
                    .setMinute(uiModel.reminderTime.value?.last() ?: 0)
                    .setTimeFormat(TimeFormat.CLOCK_12H).build()

            materialTimePicker.apply {
                show(this@CreateHabitFragment.parentFragmentManager, "MainActivity")
                addOnPositiveButtonClickListener {
                    log.info("USER: Pressed save on time picker")
                    uiModel.reminderTime.postValue(listOf(this.hour, this.minute))
                }
            }
        }
    }

    private fun setOnTextChangedListeners(
        textBox: TextInputLayout, errorText: String, interactedWith: MutableLiveData<Boolean>
    ) {
        textBox.editText?.doAfterTextChanged {
            log.info("@@@ doaftertextchanged called for ${textBox}")
            if (interactedWith.value == true) {
                log.info("textbox $errorText has been interacted with? ${interactedWith.value}")
                showTextBoxError(
                    it?.trim()?.isEmpty() == true, interactedWith.value == true, textBox, errorText
                )
            } else {
                interactedWith.postValue(true)
            }
        }
    }

    private fun setReminderTimeObserver(
        textBox: TextInputEditText, reminderTime: MutableLiveData<List<Int>>
    ) {
        val textBoxObserver = Observer<List<Int>> {
            textBox.setText(convertTimeToString(it.first(), it.last()))
        }
        reminderTime.observe(viewLifecycleOwner, textBoxObserver)

    }

    private fun setTextBoxObserver(
        textBox: TextInputLayout, text: MutableLiveData<String>
    ) {
        val textBoxObserver = Observer<String> {
            textBox.editText?.setText(it)
        }
        text.observe(viewLifecycleOwner, textBoxObserver)

    }

    private fun setDayOfWeekObserversAndListeners() {
        setDayOfWeekObserversAndListenerDay(binding.mon, uiModel.monEnabled)
        setDayOfWeekObserversAndListenerDay(binding.tue, uiModel.tueEnabled)
        setDayOfWeekObserversAndListenerDay(binding.wed, uiModel.wedEnabled)
        setDayOfWeekObserversAndListenerDay(binding.thu, uiModel.thuEnabled)
        setDayOfWeekObserversAndListenerDay(binding.fri, uiModel.friEnabled)
        setDayOfWeekObserversAndListenerDay(binding.sat, uiModel.satEnabled)
        setDayOfWeekObserversAndListenerDay(binding.sun, uiModel.sunEnabled)
    }

    private fun setDayOfWeekObserversAndListenerDay(
        button: Button, enabled: MutableLiveData<Boolean>
    ) {
        val fabObserver = Observer<Boolean> {
            if (enabled.value != false) button.apply {
                alpha = 1.0F
            }
            else button.alpha = 0.5F
        }
        enabled.observe(viewLifecycleOwner, fabObserver)

        button.setOnClickListener {
            enabled.postValue(!(enabled.value ?: false))
        }
    }

    private fun populateValues() {
        // Pull habit from fragment args. Will be null if no args passed
        val args: CreateHabitFragmentArgs by navArgs()
        val habit = args.habit

        if (habit == null) {
            log.info("No args passed in, populate fragment with default values")
            requireActivity().actionBar?.title = getString(R.string.create_habit_fragment_label)
            viewModel.resetCreateHabitFragmentUiModel()
        } else {
            log.info("Changing title to ${getString(R.string.create_habit_fragment_label_edit)}")
            requireActivity().actionBar?.title =
                getString(R.string.create_habit_fragment_label_edit)
            binding.createButton.text = getString(R.string.save)

            log.info("Applying saved values. habit name = ${habit.habitName}")
            viewModel.populateUiModel(habit)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        viewModel.fabVisible.postValue(true)
        viewModel.resetCreateHabitFragmentUiModel()
        _binding = null
    }
}
