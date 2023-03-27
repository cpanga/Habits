package com.example.habits

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.coroutineScope
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.habits.databinding.FragmentHabitListBinding
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import java.util.logging.Logger

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class HabitListFragment : Fragment() {
    companion object {
        val log: Logger = Logger.getLogger(HabitListFragment::class.java.name)
    }

    private var _binding: FragmentHabitListBinding? = null
    private val viewModel: HabitViewModel by activityViewModels {
        HabitViewModel.HabitViewModelFactory(
            (activity?.application as HabitApplication).database.habitDao()
        )
    }

    // This property is only valid between onCreateView and onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentHabitListBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val habitAdapter = HabitListAdapter { hideFab() }
        binding.recyclerView.run {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = habitAdapter
        }
        lifecycle.coroutineScope.launch {
            viewModel.getAllHabits().collect() {
                habitAdapter.submitList(it)
            }
        }
    }

    private fun hideFab() {
        log.info("Hiding FAB")
        viewModel.fabVisible.postValue(false)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}