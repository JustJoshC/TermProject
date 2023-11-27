package com.sheridancollege.cowanjos.advandtermproj

import android.app.DatePickerDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.sheridancollege.cowanjos.advandtermproj.ui.account.AccountViewModel
import androidx.recyclerview.widget.LinearLayoutManager
import com.sheridancollege.cowanjos.advandtermproj.databinding.FragmentFreeWeightsFragmentBinding
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException

class FreeWeightsFragment : Fragment() {

    private var _binding: FragmentFreeWeightsFragmentBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: FreeWeightsViewModel
    private lateinit var accountViewModel: AccountViewModel // Add AccountViewModel
    private lateinit var freeWeightsAdapter: FreeWeightsAdapter

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


        // Account Stuff

        // Get the DAO from the database instance
        val accountDao = database.accountDao()

        // Create the repository instance using the DAO
        val accountRepository = AccountRepository(accountDao)

        // Initialize the AccountViewModel
        // Make sure to provide the necessary dependencies for AccountViewModelFactory
        val accountFactory = AccountViewModelFactory(accountRepository)
        accountViewModel = ViewModelProvider(this, accountFactory).get(AccountViewModel::class.java)

        // Initialize RecyclerView and Adapter
        setupRecyclerView()

        setupClickListeners()

        // Observe the LiveData from the ViewModel and update the RecyclerView
        viewModel.freeWeightsList.observe(viewLifecycleOwner) { freeWeightsList ->
            freeWeightsAdapter.setFreeWeights(freeWeightsList)
        }

        return view
    }

    private fun setupRecyclerView() {
        // Initialize the adapter with a lambda function for delete action
        freeWeightsAdapter = FreeWeightsAdapter { freeWeight ->
            // Call the delete function in the ViewModel
            viewModel.deleteFreeWeights(freeWeight.freeWeightsId)
            Toast.makeText(context, "Deleted Successfully!", Toast.LENGTH_SHORT).show()
        }
        binding.freeWeightsRecyclerView.layoutManager = LinearLayoutManager(context)
        binding.freeWeightsRecyclerView.adapter = freeWeightsAdapter
    }


    private fun setupClickListeners() {
        binding.datePickerButton.setOnClickListener {
            showDatePicker()
        }

        binding.saveButton.setOnClickListener {
            handleSave()
        }

    }

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
                val username = "nintendogamer350@gmail.com" // Replace with the actual logic to get the logged-in username
                val accountId = accountViewModel.getUserIdByUsername(username)

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
