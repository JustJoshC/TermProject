package com.sheridancollege.cowanjos.advandtermproj.ui.workouts

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.sheridancollege.cowanjos.advandtermproj.R
import com.sheridancollege.cowanjos.advandtermproj.databinding.FragmentWorkoutsBinding

class WorkoutFragment : Fragment() {

    private var _binding: FragmentWorkoutsBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private lateinit var viewModel: WorkoutViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val binding = FragmentWorkoutsBinding.inflate(inflater, container, false)

        viewModel = ViewModelProvider(this).get(WorkoutViewModel::class.java)

        binding.btnFreeWeights.setOnClickListener {
            findNavController().navigate(R.id.action_workoutFragment_to_freeWeightsFragment)
        }

        binding.btnCycling.setOnClickListener {
            findNavController().navigate(R.id.action_workoutFragment_to_cyclingFragment)
        }

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}