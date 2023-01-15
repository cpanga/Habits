package com.example.habits

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.example.habits.databinding.FragmentSecondBinding

/**
 * A simple [Fragment] subclass as the second destination in the navigation.
 */
class CreateHabitFragment : Fragment() {

    private var _binding: FragmentSecondBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    private val viewModel: MainActivityViewModel by activityViewModels()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSecondBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.buttonSecond.setOnClickListener {
            findNavController().navigate(R.id.action_SecondFragment_to_FirstFragment)
        }

        setDayOfWeekObserversAndListeners()

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
                background = false
                isClickable = true
            }
            else button.isEnabled = true
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