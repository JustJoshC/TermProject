package com.sheridancollege.cowanjos.advandtermproj

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class FreeWeightsViewModel(private val repository: FreeWeightsRepository) : ViewModel() {

    // LiveData to observe the FreeWeights data changes in the UI
    val freeWeightsList = repository.allFreeWeights

    // Function to add a new FreeWeights workout
    fun addFreeWeights(freeWeights: FreeWeights) {
        viewModelScope.launch {
            repository.insertFreeWeights(freeWeights)
        }
    }

    // Function to update an existing FreeWeights workout
    fun updateFreeWeights(freeWeights: FreeWeights) {
        viewModelScope.launch {
            repository.updateFreeWeights(freeWeights)
        }
    }

    // Function to delete a FreeWeights workout by its ID
    fun deleteFreeWeights(freeWeightsId: Int) {
        viewModelScope.launch {
            repository.deleteFreeWeights(freeWeightsId)
        }
    }
}
