package com.sheridancollege.cowanjos.advandtermproj.ui.diet

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.sheridancollege.cowanjos.advandtermproj.AppDatabase
import com.sheridancollege.cowanjos.advandtermproj.R
import com.sheridancollege.cowanjos.advandtermproj.databinding.FragmentDietBinding

class DietFragment : Fragment() {

    private var _binding: FragmentDietBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private lateinit var dietAdapter: DietAdapter
    private lateinit var viewModel: DietViewModel
    private lateinit var auth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentDietBinding.inflate(inflater, container, false)
        val root: View = binding.root
        val database = AppDatabase.getInstance(requireContext())
        val addMealDao = database.mealDao()
        val dietDao = database.dietDao()
        val dietGoalRepository = DietGoalRepository(dietDao)
        val mealRepository = AddMealRepository(addMealDao)
        val factory = DietViewModelFactory(mealRepository, dietGoalRepository)

        viewModel = ViewModelProvider(this, factory).get(DietViewModel::class.java)

        binding.btnDietGoals.setOnClickListener{
            findNavController().navigate(R.id.action_navigation_diet_to_navigation_diet_goal)
        }

        binding.btnAddMeal.setOnClickListener {
            findNavController().navigate(R.id.action_navigation_diet_to_navigation_meal)
        }

        viewModel.addMealList.observe(viewLifecycleOwner) { addMealList ->
            // Update the meals in the adapter
            dietAdapter.setMeals(addMealList)

            if(addMealList.isEmpty()){
                binding.textCurrentCalories.text = "Last 3 Meals Calories: None"
            }
            else{
                // Calculate total calories and update the TextView
                val totalCalories = dietAdapter.calculateCalories()
                binding.textCurrentCalories.text = "Last 3 Meals Calories: $totalCalories"
            }

        }

        viewModel.dietGoalList.observe(viewLifecycleOwner) { dietGoalList ->

            if(dietGoalList.isEmpty()){
                binding.textCurrentGoal.text = "Current Goal: None"
            }
            else{
                val calorieGoal = dietGoalList.last().targetCalories
                binding.textCurrentGoal.text = "Current Goal $calorieGoal"
            }

        }


        setupRecyclerView()



        viewModel.addMealList.observe(viewLifecycleOwner) { addMealList ->
            dietAdapter.setMeals(addMealList)
        }


        return root
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun setupRecyclerView(){
        dietAdapter = DietAdapter()
        binding.recyclerViewMeals.layoutManager = LinearLayoutManager(context)
        binding.recyclerViewMeals.adapter = dietAdapter
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}