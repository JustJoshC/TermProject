package com.sheridancollege.cowanjos.advandtermproj.ui.diet

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sheridancollege.cowanjos.advandtermproj.Meal
import kotlinx.coroutines.launch

class AddMealViewModel(private val repository: AddMealRepository) : ViewModel() {

    val addMealList: LiveData<List<Meal>> = repository.allAddedMeals

    fun addMeal(meal: Meal){
        viewModelScope.launch {
            repository.insertMeal(meal)
        }
    }

    fun deleteLastMeal(){
        viewModelScope.launch {
            repository.deleteLastInsertedDietGoal()
        }
    }
}