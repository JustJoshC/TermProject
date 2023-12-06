package com.sheridancollege.cowanjos.advandtermproj.ui.diet

import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.core.net.toUri
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.sheridancollege.cowanjos.advandtermproj.AppDatabase
import com.sheridancollege.cowanjos.advandtermproj.R
import com.sheridancollege.cowanjos.advandtermproj.databinding.FragmentDietBinding
import java.io.File

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

        displayLatestImage()

        viewModel.addMealList.observe(viewLifecycleOwner) { addMealList ->
            dietAdapter.setMeals(addMealList)
        }


        return root
    }

    private fun setupRecyclerView(){
        dietAdapter = DietAdapter()
        binding.recyclerViewMeals.layoutManager = LinearLayoutManager(context)
        binding.recyclerViewMeals.adapter = dietAdapter
    }

    private fun displayLatestImage() {
        // Get the path of the latest image from the specified directory
        val imagePath = getLatestImagePath()

        // Check if the path is not null or blank
        if (!imagePath.isNullOrBlank()) {
            // Create a File object from the image path
            val imageFile = File(imagePath)

            // Check if the file actually exists in the file system
            if (imageFile.exists()) {
                // If the file exists, set it as the image source for the ImageView in your layout
                binding.capturedImage.setImageURI(imageFile.toUri())
            }
        }
    }


    private fun getLatestImagePath(): String? {
        // Define the columns to retrieve from the MediaStore
        val projection = arrayOf(
            MediaStore.Images.Media.DATA,
            MediaStore.Images.ImageColumns.DATE_TAKEN
        )

        // Perform a query on the MediaStore to retrieve images
        val cursor = requireContext().contentResolver.query(
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
            projection,
            "${MediaStore.Images.ImageColumns.DATA} like '%Pictures/TermProject-Image%'",
            null,
            "${MediaStore.Images.ImageColumns.DATE_TAKEN} DESC"
        )

        // Initialize a variable to hold the path of the latest image
        var latestImagePath: String? = null

        // Use the cursor to navigate through the query results
        cursor?.use { c ->
            // If the cursor can move to the last row, it means there is at least one image
            if (c.moveToLast()) {
                // Retrieve the image path from the DATA column and store it in the variable
                latestImagePath = c.getString(c.getColumnIndexOrThrow(MediaStore.Images.Media.DATA))
            }
        }
        // Close the cursor to free up resources
        cursor?.close()

        // Return the path of the latest image
        return latestImagePath
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}