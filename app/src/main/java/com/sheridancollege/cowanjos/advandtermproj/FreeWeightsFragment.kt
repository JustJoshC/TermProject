package com.sheridancollege.cowanjos.advandtermproj

import android.app.DatePickerDialog
import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewModelScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.sheridancollege.cowanjos.advandtermproj.databinding.FragmentFreeWeightsFragmentBinding
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException

class FreeWeightsFragment : Fragment() {
    private var editingFreeWeight: FreeWeights? = null

    private var _binding: FragmentFreeWeightsFragmentBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: FreeWeightsViewModel
    private lateinit var freeWeightsAdapter: FreeWeightsAdapter
    private lateinit var auth: FirebaseAuth

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentFreeWeightsFragmentBinding.inflate(inflater, container, false)
        val view = binding.root

        // Get an instance of the Room database
        val database = AppDatabase.getInstance(requireContext())

        // Free Weight Stuff

        // Get the DAO from the database instance
        val freeWeightsDao = database.freeWeightsDao()

        // Create the repository instance using the DAO
        val freeWeightsRepository = FreeWeightsRepository(freeWeightsDao)

        // Initialize the ViewModel
        val factory = FreeWeightsViewModelFactory(freeWeightsRepository)
        viewModel = ViewModelProvider(this, factory).get(FreeWeightsViewModel::class.java)


        auth = FirebaseAuth.getInstance()

        // Initialize RecyclerView and Adapter
        setupRecyclerView()

        setupClickListeners()

        // Observe the LiveData from the ViewModel and update the RecyclerView
        viewModel.freeWeightsList.observe(viewLifecycleOwner) { freeWeightsList ->
            freeWeightsAdapter.setFreeWeights(freeWeightsList)
        }

        return view
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun setupRecyclerView() {
        // Initialize the adapter with lambda functions for both edit and delete actions
        freeWeightsAdapter = FreeWeightsAdapter(
            onEditClicked = { freeWeight ->
                // Logic for editing the free weight
                // This could open a dialog or navigate to another screen with the freeWeight details
                handleEditFreeWeight(freeWeight)
            },
            onDeleteClicked = { freeWeight ->
                // Call the delete function in the ViewModel
                viewModel.deleteFreeWeights(freeWeight.freeWeightsId)
                Toast.makeText(context, "Deleted Successfully!", Toast.LENGTH_SHORT).show()
            }
        )

        binding.freeWeightsRecyclerView.layoutManager = LinearLayoutManager(context)
        binding.freeWeightsRecyclerView.adapter = freeWeightsAdapter
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun handleEditFreeWeight(freeWeight: FreeWeights) {
        editingFreeWeight = freeWeight
        // Populate the form with the details of the workout being edited
        binding.muscleGroupInput.setText(freeWeight.muscleGroup)
        binding.durationInput.setText(freeWeight.workoutDuration)
        binding.dateLabel.text = freeWeight.date.toString()
    }


    @RequiresApi(Build.VERSION_CODES.O)
    private fun setupClickListeners() {
        binding.datePickerButton.setOnClickListener {
            showDatePicker()
        }

        binding.saveButton.setOnClickListener {
            if (editingFreeWeight == null) {
                handleSave() // Adding new workout
            } else {
                handleUpdate() // Updating existing workout
            }
        }

    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun handleUpdate() {
        val muscleGroupInput = binding.muscleGroupInput.text.toString().trim()
        val durationInput = binding.durationInput.text.toString().trim()
        val dateString = binding.dateLabel.text.toString()

        // Validate inputs
        if (muscleGroupInput.isBlank() || durationInput.isBlank() || dateString == "Select Date") {
            Toast.makeText(context, "Please fill in all fields", Toast.LENGTH_SHORT).show()
            return
        }

        try {
            val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
            val date = LocalDate.parse(dateString, formatter)

            // Use viewModelScope to execute isEditingToExistingWorkout check
            viewModel.viewModelScope.launch {
                if (!isEditingToExistingWorkout(muscleGroupInput, date)) {
                    // Proceed with update as the edited workout is unique
                    val updatedFreeWeight = editingFreeWeight!!.copy(
                        muscleGroup = muscleGroupInput,
                        workoutDuration = durationInput,
                        date = date
                    )
                    viewModel.updateFreeWeights(updatedFreeWeight)
                    Toast.makeText(context, "Workout updated successfully", Toast.LENGTH_SHORT).show()
                    clearInputFields()
                    editingFreeWeight = null
                }

                else if(isEditingToExistingWorkout(muscleGroupInput, date)){
                    // Show an error message as the workout is not unique
                    Toast.makeText(context, "A workout with this muscle group and date already exists.", Toast.LENGTH_SHORT).show()
                }
            }

        } catch (e: DateTimeParseException) {
            Toast.makeText(context, "Invalid date format. Please select a valid date.", Toast.LENGTH_SHORT).show()
        }
    }


    @RequiresApi(Build.VERSION_CODES.O)
    private fun isEditingToExistingWorkout(muscleGroup: String, date: LocalDate): Boolean {
        val freeWeightsList = viewModel.freeWeightsList.value ?: return false

        return freeWeightsList.any { existingWorkout ->
            existingWorkout.muscleGroup.equals(muscleGroup, ignoreCase = true) &&
                    existingWorkout.date.isEqual(date) &&
                    existingWorkout.freeWeightsId != editingFreeWeight?.freeWeightsId
        }
    }





    @RequiresApi(Build.VERSION_CODES.O)
    private fun showDatePicker() {
        val currentDate = LocalDate.now()
        val datePickerDialog = DatePickerDialog(requireContext(), { _, year, month, dayOfMonth ->
            val selectedDate = LocalDate.of(year, month + 1, dayOfMonth)
            // Update the date TextView or ViewModel with the selected date
            val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
            binding.dateLabel.text = selectedDate.format(formatter)
        }, currentDate.year, currentDate.monthValue - 1, currentDate.dayOfMonth)

        datePickerDialog.show()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun handleSave() {
        val muscleGroupInput = binding.muscleGroupInput.text.toString()
        val durationInput = binding.durationInput.text.toString()
        val dateString = binding.dateLabel.text.toString()

        // Validate inputs
        if (muscleGroupInput.isBlank() || durationInput.isBlank() || dateString.isBlank()) {
            Toast.makeText(context, "Please fill in all fields", Toast.LENGTH_SHORT).show()
            return
        }

        try {
            val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
            val date = LocalDate.parse(dateString, formatter)

            // Launch a coroutine to perform database operations
            lifecycleScope.launch {
                val accountId = auth.currentUser!!.email

                // Check if accountId is not null, meaning the user was found
                if (accountId != null) {
                    // Create a new FreeWeights object with the obtained data
                    val newFreeWeight = FreeWeights(
                        freeWeightsId = 0, // Assuming new records start with 0
                        accountId = accountId,
                        date = date,
                        muscleGroup = muscleGroupInput,
                        workoutDuration = durationInput
                    )

                    // Check if it's a new addition (freeWeightsId == 0) or an update (freeWeightsId != 0)
                    if (newFreeWeight.freeWeightsId == 0) {
                        viewModel.addFreeWeights(newFreeWeight) // Add new free weight record
                    } else {
                        viewModel.updateFreeWeights(newFreeWeight) // Update existing free weight record
                    }

                    // Optionally clear the input fields after saving
                    clearInputFields()
                } else {
                    // If accountId is null, it means the user wasn't found in the database
                    Toast.makeText(context, "User not found. Please log in again.", Toast.LENGTH_SHORT).show()
                }
            }

        } catch (e: DateTimeParseException) {
            Toast.makeText(context, "Invalid date format. Please select a valid date.",
                Toast.LENGTH_SHORT).show()
        }


    }

    private fun clearInputFields() {
        binding.muscleGroupInput.setText("")
        binding.durationInput.setText("")
        binding.dateLabel.setText("")
    }



    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
