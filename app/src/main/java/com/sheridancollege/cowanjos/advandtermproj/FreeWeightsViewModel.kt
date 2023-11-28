package com.sheridancollege.cowanjos.advandtermproj

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.time.LocalDate

class FreeWeightsViewModel(private val repository: FreeWeightsRepository) : ViewModel() {
    //Here is the FreeWeightsViewModel class :)

    // LiveData to observe the FreeWeights data changes in the UI
    val freeWeightsList: LiveData<List<FreeWeights>> = repository.allFreeWeights

    // LiveData to notify about duplicate workout detection
    val duplicateWorkoutDetected = MutableLiveData<Boolean>()

    // Function to add a new FreeWeights workout
    fun addFreeWeights(freeWeights: FreeWeights) {
        viewModelScope.launch {
            if (!isExistingWorkout(freeWeights.muscleGroup, freeWeights.date)) {
                repository.insertFreeWeights(freeWeights)
            } else {
                // Notify about the duplicate
                duplicateWorkoutDetected.value = true
            }
        }
    }

    /**
     * Checks asynchronously if there is an existing workout with the same muscle group and date,
     * excluding a specific workout ID.
     * @param muscleGroup The muscle group of the workout.
     * @param date The date of the workout.
     * @param excludeId The ID of the workout being edited, if any (null for new workouts).
     * @return True if a duplicate workout exists, false otherwise.
     */
    suspend fun checkForExistingWorkout(muscleGroup: String, date: LocalDate, excludeId: Int?): Boolean {
        return withContext(Dispatchers.IO) {
            // Call the repository method to check for an existing workout
            repository.checkForExistingWorkout(muscleGroup, date, excludeId)
        }
    }

    // Function to update an existing FreeWeights workout
    fun updateFreeWeights(freeWeights: FreeWeights) {
        viewModelScope.launch {
            if (!isExistingWorkout(freeWeights.muscleGroup, freeWeights.date, freeWeights.freeWeightsId)) {
                repository.updateFreeWeights(freeWeights)
            } else {
                // Notify about the duplicate
                duplicateWorkoutDetected.value = true
            }
        }
    }

    // Function to delete a FreeWeights workout by its ID
    fun deleteFreeWeights(freeWeightsId: Int) {
        viewModelScope.launch {
            repository.deleteFreeWeights(freeWeightsId)
        }
    }

    // Check if there's an existing workout with the same muscle group and date
    private fun isExistingWorkout(muscleGroup: String, date: LocalDate, editingId: Int? = null): Boolean {
        val workouts = freeWeightsList.value ?: return false
        return workouts.any {
            it.muscleGroup.equals(muscleGroup, ignoreCase = true) &&
                    it.date.isEqual(date) &&
                    (editingId == null || it.freeWeightsId != editingId)
        }
    }


}
