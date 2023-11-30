package com.sheridancollege.cowanjos.advandtermproj.ui.diet

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.sheridancollege.cowanjos.advandtermproj.FreeWeightsViewModel

class AddMealViewModelFactory(private val repository: AddMealRepository) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AddMealViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return AddMealViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }

}