package com.sheridancollege.cowanjos.advandtermproj.ui.diet

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class DietViewModelFactory(private val repository: AddMealRepository, private val dietGoalRepository: DietGoalRepository) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(DietViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return DietViewModel(repository, dietGoalRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }

}
