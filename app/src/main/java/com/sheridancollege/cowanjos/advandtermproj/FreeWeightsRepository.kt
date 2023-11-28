package com.sheridancollege.cowanjos.advandtermproj

import androidx.lifecycle.LiveData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.time.LocalDate

class FreeWeightsRepository(private val freeWeightsDao: FreeWeightsDao) {

    // LiveData to observe changes in the Free Weights workouts list
    val allFreeWeights: LiveData<List<FreeWeights>> = freeWeightsDao.getAllFreeWeights()

    // Function to insert a new FreeWeights workout into the database
    suspend fun insertFreeWeights(freeWeights: FreeWeights) {
        freeWeightsDao.insertFreeWeights(freeWeights)
    }

    // Function to update an existing FreeWeights workout
    suspend fun updateFreeWeights(freeWeights: FreeWeights) {
        freeWeightsDao.updateFreeWeights(freeWeights.freeWeightsId, freeWeights.date.toString(),
            freeWeights.muscleGroup, freeWeights.workoutDuration)
    }

    /**
     * Checks if a workout with the specified muscle group and date already exists in the database,
     * excluding a specific workout ID.
     * @param muscleGroup The muscle group of the workout.
     * @param date The date of the workout.
     * @param excludeId The ID of the workout to exclude from the check.
     * @return Boolean indicating whether a duplicate workout exists.
     */
    suspend fun checkForExistingWorkout(muscleGroup: String, date: LocalDate, excludeId: Int?): Boolean {
        return withContext(Dispatchers.IO) {
            val count = freeWeightsDao.checkForExistingWorkout(muscleGroup, date.toString(), excludeId)
            count > 0
        }
    }

    // Function to delete a specific FreeWeights workout by ID
    suspend fun deleteFreeWeights(freeWeightsId: Int) {
        freeWeightsDao.deleteFreeWeights(freeWeightsId)
    }
}
