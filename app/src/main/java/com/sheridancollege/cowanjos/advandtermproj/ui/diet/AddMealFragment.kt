package com.sheridancollege.cowanjos.advandtermproj.ui.diet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.sheridancollege.cowanjos.advandtermproj.databinding.FragmentAddMealBinding
import com.sheridancollege.cowanjos.advandtermproj.databinding.FragmentDietGoalsBinding


class AddMealFragment: Fragment() {

    private var _binding: FragmentAddMealBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val addMealViewModel =
            ViewModelProvider(this).get(AddMealViewModel::class.java)

        _binding = FragmentAddMealBinding.inflate(inflater, container, false)
        val root: View = binding.root


        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}