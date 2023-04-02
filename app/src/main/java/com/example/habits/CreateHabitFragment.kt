package com.example.habits

import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.Button
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.coroutineScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.habits.databinding.FragmentHabitCreateBinding
import com.example.habits.util.convertTimeToString
import com.example.habits.util.ensureAtLeastOneIsTrue
import com.example.habits.util.isTextBoxEmpty
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
import kotlinx.coroutines.launch
import java.util.logging.Logger

/**
 * Fragment for creating a new Habit, or editing an existing one.
 */
class CreateHabitFragment : Fragment() {
    companion object {
        val log: Logger = Logger.getLogger(CreateHabitFragment::class.java.name)
    }

    private lateinit var uiModel: CreateHabitFragmentUiModel
    private lateinit var createOrEdit: CreateOrEdit
    private var _binding: FragmentHabitCreateBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    private val viewModel: HabitViewModel by activityViewModels {
        HabitViewModel.HabitViewModelFactory(
            (activity?.application as HabitApplication).database.habitDao()
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHabitCreateBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // Define uiModel as ViewModel's uiModel, and populate the uiModel with appropriate values
        uiModel = viewModel.createHabitUiModel
        populateValues()

        // Set UI observers and listeners
        log.info("Setting Observers and Listeners")
        setOnTextChangedListeners(
            binding.habitName, getString(R.string.habit_name_error), uiModel.nameTextInteractedWith
        )
        setOnTextChangedListeners(
            binding.habitDesc, getString(R.string.habit_desc_error), uiModel.descTextInteractedWith
        )
        setDayOfWeekObserversAndListeners()
        setReminderTimeObserver(binding.reminderTimeBox, uiModel.reminderTime)
        setTextBoxObserver(binding.habitName, uiModel.habitName)
        setTextBoxObserver(binding.habitDesc, uiModel.habitDesc)
        setTimePickerBoxListener(binding.reminderTimeBox)
        setCreateButtonClickListener()
        setDeleteButtonClickListener()
    }

    // Set the click listener for the delete button. Only shown if editing a Habit.
    private fun setDeleteButtonClickListener() {
        binding.deleteButton.setOnClickListener() {
            log.info("Deleting habit: '${uiModel.habitName.value}'")
            lifecycle.coroutineScope.launch() {
                try {
                    viewModel.deleteHabit(uiModel.uid!!)
                } catch (e: java.lang.NullPointerException) {
                    log.warning("There is no uuid for some reason. This should never happen. $e")
                }
            }
            // Navigate back to list fragment
            findNavController().navigate(R.id.action_SecondFragment_to_FirstFragment)
        }
    }

    private fun setCreateButtonClickListener() {
        binding.createButton.setOnClickListener {
            // Check that there is text in both text boxes before saving. If there are, errors will be displayed
            log.info("USER: ${binding.createButton.text} button pressed")

            checkTextBoxErrors(binding.habitName,
                getString(R.string.habit_name_error),
                uiModel.nameTextInteractedWith.apply { value = true })
            checkTextBoxErrors(binding.habitDesc,
                getString(R.string.habit_desc_error),
                uiModel.descTextInteractedWith.apply { value = true })


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
                // Update the uiModel and return the current data as a Habit
                log.info("Saving habit")
                uiModel.habitName.value = binding.habitName.editText!!.text.toString()
                uiModel.habitDesc.value = binding.habitDesc.editText!!.text.toString()
                val habit = uiModel.returnAsHabit()
                // Save the habit to the Database
                lifecycle.coroutineScope.launch() {
                    when (createOrEdit) {
                        CreateOrEdit.EDIT -> {
                            log.info("Saving changes to habit: '${habit.habitName}'")
                            viewModel.updateHabit(habit)
                        }
                        CreateOrEdit.CREATE -> {
                            log.info("Creating new habit: '${habit.habitName}'")
                            viewModel.createHabit(uiModel.returnAsHabit())
                        }

                    }
                }
                // Navigate back to list fragment
                findNavController().navigate(R.id.action_SecondFragment_to_FirstFragment)
            }
        }
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
            checkTextBoxErrors(textBox, errorText, interactedWith)
        }
    }

    private fun checkTextBoxErrors(
        textBox: TextInputLayout, errorText: String, interactedWith: MutableLiveData<Boolean>
    ) {
        val textBoxEmpty = isTextBoxEmpty(textBox)
        if (interactedWith.value == true) {
            showTextBoxError(
                textBoxEmpty, interactedWith.value == true, textBox, errorText
            )
        } else if (!textBoxEmpty) {
            log.info("textBox ${textBox.id} has been interacted with. Setting flag")
            interactedWith.postValue(true)
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

        createOrEdit = if (habit == null) CreateOrEdit.CREATE else CreateOrEdit.EDIT

        var selectAllOnFocus: Boolean? = null
        var actionBarTitle: String? = null
        var createButtonString: String? = null
        lateinit var createButtonIcon: Drawable

        when (createOrEdit) {
            CreateOrEdit.CREATE -> {
                log.info("No args passed in, populate fragment with default values")
                viewModel.resetCreateHabitFragmentUiModel()
                actionBarTitle = getString(R.string.create_habit_fragment_label)
                selectAllOnFocus = false
                createButtonString = getString(R.string.create)
                createButtonIcon = resources.getDrawable(R.drawable.baseline_add_task_24)
                binding.deleteButton.visibility = GONE
            }
            CreateOrEdit.EDIT -> {
                log.info("Applying saved values. habit name = ${habit!!.habitName}")
                viewModel.populateUiModel(habit)
                actionBarTitle = getString(R.string.create_habit_fragment_label_edit)
                selectAllOnFocus = true
                createButtonString = getString(R.string.save)
                createButtonIcon = resources.getDrawable(R.drawable.baseline_save_24)
                binding.deleteButton.visibility = VISIBLE
            }
        }

        log.info("Changing fragment title to $actionBarTitle")
        requireActivity().actionBar?.title = actionBarTitle
        binding.habitName.editText?.setSelectAllOnFocus(selectAllOnFocus)
        binding.habitDesc.editText?.setSelectAllOnFocus(selectAllOnFocus)
        binding.createButton.run {
            text = createButtonString
            icon = createButtonIcon
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        viewModel.fabVisible.postValue(true)
        viewModel.resetCreateHabitFragmentUiModel()
        _binding = null
    }
}

// Enum class which determines if the fragment will show the UI for creating or editing a Habit
enum class CreateOrEdit {
    CREATE, EDIT
}