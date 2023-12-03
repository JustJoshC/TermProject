package com.sheridancollege.cowanjos.advandtermproj.ui.diet

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import com.sheridancollege.cowanjos.advandtermproj.AppDatabase
import com.sheridancollege.cowanjos.advandtermproj.Meal
import com.sheridancollege.cowanjos.advandtermproj.MealDao
import com.sheridancollege.cowanjos.advandtermproj.R
import com.sheridancollege.cowanjos.advandtermproj.databinding.FragmentAddMealBinding
import com.sheridancollege.cowanjos.advandtermproj.databinding.FragmentDietGoalsBinding
import kotlinx.coroutines.launch


class AddMealFragment: Fragment() {

    private var _binding: FragmentAddMealBinding? = null

    private lateinit var viewModel: AddMealViewModel
    private lateinit var auth: FirebaseAuth

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentAddMealBinding.inflate(inflater, container, false)
        val root: View = binding.root

        // Get Room Database instance
        val database = AppDatabase.getInstance(requireContext())

        // Get Dao
        val addMealDao = database.mealDao()

        // repo instance
        val addMealRepository = AddMealRepository(addMealDao)

        // factory init
        val factory = AddMealViewModelFactory(addMealRepository)

        viewModel = ViewModelProvider(this, factory).get(AddMealViewModel::class.java)

        auth = FirebaseAuth.getInstance()


        binding.btnSaveMeal.setOnClickListener {
            saveMeal()
            findNavController().navigate(R.id.action_navigation_meal_to_navigation_diet3)
        }

        binding.btnDeleteMeal.setOnClickListener {
            lifecycleScope.launch {
                viewModel.deleteLastMeal()
                findNavController().navigate(R.id.action_navigation_meal_to_navigation_diet3)
            }
        }

        return root
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun saveMeal(){
        val mealName = binding.editMealName.text.toString()
        val mealCalories = binding.editMealCalories.text.toString()
        val mealCaloriesInt = mealCalories.toInt()
        val mealDateDay = binding.MealDatePicker.dayOfMonth.toString()
        val mealDateMonth = binding.MealDatePicker.month.toString()
        val mealDateYear = binding.MealDatePicker.year.toString()

        val mealDate = "$mealDateDay/$mealDateMonth/$mealDateYear"

        if(mealName.isBlank() || mealCalories.isBlank()){
            Toast.makeText(context, "Please fill in all fields", Toast.LENGTH_SHORT).show()
            return
        }

        lifecycleScope.launch {
            val accountId = auth.currentUser!!.email

            if(accountId != null){

                val newMeal = Meal(
                    dietId = 0,
                    mealId = 0,
                    accountId = accountId,
                    mealTime = mealDate,
                    mealDescription = mealName,
                    mealCalories = mealCaloriesInt
                )
                // add meal
                viewModel.addMeal(newMeal)
            }
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}