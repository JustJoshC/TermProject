package com.sheridancollege.cowanjos.advandtermproj.ui.diet

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.sheridancollege.cowanjos.advandtermproj.Diet
import com.sheridancollege.cowanjos.advandtermproj.Meal

class DietViewModel(private val repository: AddMealRepository, private val dietGoalRepository: DietGoalRepository) : ViewModel() {

    val addMealList : LiveData<List<Meal>> = repository.allAddedMeals
    val dietGoalList: LiveData<List<Diet>> = dietGoalRepository.dietGoalList

}