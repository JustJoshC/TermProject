package com.sheridancollege.cowanjos.advandtermproj

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewModelScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.sheridancollege.cowanjos.advandtermproj.databinding.FragmentCyclingBinding
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException

class CyclingFragment : Fragment() {
    private var editingCycling: Cycling? = null

    private var _binding: FragmentCyclingBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: CyclingViewModel
    private lateinit var adapter: CyclingAdapter
    private lateinit var auth: FirebaseAuth
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentCyclingBinding.inflate(inflater, container, false)
        val view = binding.root

        val appDatabase = AppDatabase.getInstance(requireContext())
        val cyclingDao = appDatabase.cyclingDao()
        val factory = CyclingViewModelFactory(cyclingDao)
        viewModel = ViewModelProvider(this, factory).get(CyclingViewModel::class.java)

        viewModel.duplicateWorkoutDetected.observe(viewLifecycleOwner) { isDuplicate ->
            if(isDuplicate) {
                Toast.makeText(
                    context,
                    "A cycling workout with this duration and date already exists.",
                    Toast.LENGTH_SHORT
                ).show()
                viewModel.duplicateWorkoutDetected.value = false
            }
        }

        auth = FirebaseAuth.getInstance()
        setupRecyclerView()
        setupClickListeners()

        viewModel.allCyclingData.observe(viewLifecycleOwner) { cyclingData ->
            adapter.setCycling(cyclingData)
        }
        return view
    }

    private fun setupRecyclerView() {

        adapter = CyclingAdapter(
            onEditClicked = { cycling ->

                handleEditCycling(cycling)
            },
            onDeleteClicked = { cycling ->

                viewModel.deleteCyclingData(cycling.cyclingId)
                Toast.makeText(context, "Deleted Successfully!", Toast.LENGTH_SHORT).show()
            }
        )

        binding.cyclingRecyclerView.layoutManager = LinearLayoutManager(context)
        binding.cyclingRecyclerView.adapter = adapter
    }

    private fun handleEditCycling(cycling: Cycling) {
        editingCycling = cycling
        binding.distanceInput.setText(cycling.rideDistance.toString())
        binding.durationInput.setText(cycling.rideDuration.toString())
        binding.dateLabel.text = cycling.date.toString()
    }

    private fun setupClickListeners() {
        binding.datePickerButton.setOnClickListener {
            showDatePicker()
        }

        binding.saveButton.setOnClickListener {
            if (editingCycling == null) {
                handleSave()
            } else {
                handleUpdate()
            }
        }

    }

    private fun handleUpdate() {

        val distanceInput = binding.distanceInput.text.toString().trim()
        val durationInput = binding.durationInput.text.toString().trim()
        val dateString = binding.dateLabel.text.toString()

        // Validate the inputs to ensure they are not empty or unchanged.
        if (distanceInput.isBlank() || durationInput.isBlank() || dateString == "Select Date") {
            Toast.makeText(context, "Please fill in all fields", Toast.LENGTH_SHORT).show()
            return
        }

        try {
            // Parse the date string into a LocalDate object using the specified format.
            val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
            val date = LocalDate.parse(dateString, formatter)


            // Launch a coroutine within the ViewModel's scope to perform the update logic.
            viewModel.viewModelScope.launch {

                if (!viewModel.checkForExistingWorkout(
                        date,
                        durationInput,
                        distanceInput
                    )
                ) {

                    val updatedCyclingRecord = editingCycling!!.copy(
                        date = date,
                        rideDuration = durationInput,
                        rideDistance = distanceInput
                    )

                    viewModel.updateCycling(updatedCyclingRecord)

                    Toast.makeText(
                        context,
                        "Cycling Workout updated successfully",
                        Toast.LENGTH_SHORT
                    ).show()

                    clearInputFields()
                    editingCycling = null
                } else {

                    Toast.makeText(
                        context,
                        "A similar cycling workout already exists.",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        } catch (e: DateTimeParseException) {

            Toast.makeText(
                context,
                "Invalid date format. Please select a valid date.",
                Toast.LENGTH_SHORT
            ).show()
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
        val distanceInput = binding.distanceInput.text.toString()
        val durationInput = binding.durationInput.text.toString()
        val dateString = binding.dateLabel.text.toString()

        if (distanceInput.isBlank() || durationInput.isBlank() || dateString.isBlank()) {
            Toast.makeText(context, "Please fill in all fields", Toast.LENGTH_SHORT).show()
            return
        }

        try {
            val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
            val date = LocalDate.parse(dateString, formatter)


            lifecycleScope.launch {
                val accountId = auth.currentUser!!.email


                if (accountId != null) {

                    val newCyclingRecord = Cycling(
                        cyclingId = null,
                        accountId = accountId,
                        date = date,
                        rideDistance = distanceInput,
                        rideDuration = durationInput
                    )

                    if (newCyclingRecord.cyclingId == null) {
                        viewModel.insertCyclingData(newCyclingRecord)
                    } else {
                        viewModel.updateCycling(newCyclingRecord)
                    }

                    clearInputFields()
                } else {
                    // If accountId is null, it means the user wasn't found in the database
                    Toast.makeText(
                        context,
                        "User not found. Please log in again.",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

        } catch (e: DateTimeParseException) {
            Toast.makeText(
                context, "Invalid date format. Please select a valid date.",
                Toast.LENGTH_SHORT
            ).show()
        }


    }
    private fun clearInputFields() {
        binding.distanceInput.setText("")
        binding.durationInput.setText("")
        binding.dateLabel.setText("")
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}