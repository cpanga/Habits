package com.example.habits

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.habits.databinding.FragmentHabitListBinding
import java.util.logging.Logger

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class HabitListFragment : Fragment() {
    companion object {
        val log: Logger = Logger.getLogger(HabitListFragment::class.java.name)
    }

    private var _binding: FragmentHabitListBinding? = null
    private val viewModel: MainActivityViewModel by activityViewModels()

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
        binding.recyclerView.run {
            adapter = HabitAdapter { hideFab() }
            layoutManager = LinearLayoutManager(context)
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