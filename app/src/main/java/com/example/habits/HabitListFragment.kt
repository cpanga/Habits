package com.example.habits

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.coroutineScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.habits.databinding.FragmentHabitListBinding
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
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentHabitListBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val habitAdapter = HabitListAdapter { hideFab()}
        binding.recyclerView.run {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = habitAdapter
        }
        binding.noHabitsButton.setOnClickListener {
            findNavController().navigate(R.id.action_FirstFragment_to_SecondFragment)
            viewModel.fabVisible.postValue(false)
        }

        val welcomeScreenObserver = Observer<Boolean> {
            binding.noHabitsLayout.visibility = if (it) VISIBLE else GONE
            viewModel.fabVisible.postValue(!it)
        }
        viewModel.welcomeScreenVisible.observe(viewLifecycleOwner, welcomeScreenObserver)

        lifecycle.coroutineScope.launch {
            viewModel.getAllHabits().collect() { habits ->
                habitAdapter.submitList(habits)
                viewModel.welcomeScreenVisible.postValue(habits.isEmpty())
            }
        }
    }



    private fun hideFab() {
        log.info("Hiding FAB")
        viewModel.fabVisible.postValue(false)
    }

    private fun habitDone() {
      //  viewModel.habitDonePressed()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}