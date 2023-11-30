package com.sheridancollege.cowanjos.advandtermproj.ui.diet

import androidx.lifecycle.LiveData
import com.sheridancollege.cowanjos.advandtermproj.Meal
import com.sheridancollege.cowanjos.advandtermproj.MealDao

class AddMealRepository (private val mealDao: MealDao){

    val allAddedMeals: LiveData<List<Meal>> = mealDao.getAllMeals()

    // insert meal function
    suspend fun insertMeal(meal: Meal){
        mealDao.insertMeal(meal)
    }

    // delete meal by id function
    suspend fun deleteMeal(mealId: Int){
        mealDao.deleteMeal(mealId)
    }

}