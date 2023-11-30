package com.sheridancollege.cowanjos.advandtermproj.ui.diet

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sheridancollege.cowanjos.advandtermproj.Diet
import kotlinx.coroutines.launch

class DietGoalViewModel(private val repository: DietGoalRepository) : ViewModel() {

    val addDietGoalList: LiveData<List<Diet>> = repository.dietGoalList

    fun addDietGoal(diet: Diet){
        viewModelScope.launch {
            repository.insertDietGoal(diet)
        }
    }

    fun deleteLastDietGoal() {
        viewModelScope.launch {
            repository.deleteLastInsertedDietGoal()
        }
    }


}