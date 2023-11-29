package com.sheridancollege.cowanjos.advandtermproj.ui.diet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.sheridancollege.cowanjos.advandtermproj.R
import com.sheridancollege.cowanjos.advandtermproj.databinding.FragmentDietBinding

class DietFragment : Fragment() {

    private var _binding: FragmentDietBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val dietViewModel =
            ViewModelProvider(this).get(DietViewModel::class.java)

        _binding = FragmentDietBinding.inflate(inflater, container, false)
        val root: View = binding.root

        binding.btnDietGoals.setOnClickListener{
            findNavController().navigate(R.id.action_navigation_diet_to_navigation_diet_goal)
        }

        binding.btnAddMeal.setOnClickListener {
            findNavController().navigate(R.id.action_navigation_diet_to_navigation_meal)
        }

        val textView: TextView = binding.textDiet
        dietViewModel.text.observe(viewLifecycleOwner) {
            textView.text = it
        }
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}