package com.sheridancollege.cowanjos.advandtermproj.ui.diet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import com.sheridancollege.cowanjos.advandtermproj.AppDatabase
import com.sheridancollege.cowanjos.advandtermproj.R
import com.sheridancollege.cowanjos.advandtermproj.databinding.FragmentDietBinding
import com.sheridancollege.cowanjos.advandtermproj.databinding.FragmentDietGoalsBinding

class DietGoalFragment: Fragment() {

    private var _binding: FragmentDietGoalsBinding? = null

    private lateinit var viewModel: DietGoalViewModel
    private lateinit var auth: FirebaseAuth

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentDietGoalsBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val database = AppDatabase.getInstance(requireContext())

        val dietDao = database.dietDao()




        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}