package com.sheridancollege.cowanjos.advandtermproj

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
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


    // Function to delete a specific FreeWeights workout by ID
    suspend fun deleteFreeWeights(freeWeightsId: Int) {
        freeWeightsDao.deleteFreeWeights(freeWeightsId)
    }

}
