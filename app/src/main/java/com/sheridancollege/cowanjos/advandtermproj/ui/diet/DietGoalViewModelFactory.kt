package com.sheridancollege.cowanjos.advandtermproj.ui.diet

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class DietGoalViewModelFactory(private val repository: DietGoalRepository) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(DietGoalViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return DietGoalViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }

}