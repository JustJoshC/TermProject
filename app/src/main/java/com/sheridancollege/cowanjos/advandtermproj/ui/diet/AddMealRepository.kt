package com.sheridancollege.cowanjos.advandtermproj.ui.diet

import androidx.lifecycle.LiveData
import androidx.room.Dao
import com.sheridancollege.cowanjos.advandtermproj.Diet
import com.sheridancollege.cowanjos.advandtermproj.Meal
import com.sheridancollege.cowanjos.advandtermproj.MealDao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class AddMealRepository (private val mealDao: MealDao){

    val allAddedMeals: LiveData<List<Meal>> = mealDao.getAllMeals()

    // insert meal function
    suspend fun insertMeal(meal: Meal){
        mealDao.insertMeal(meal)
    }

    // delete meal func
    suspend fun getLastInsertedMealGoal(): Meal? {
        return withContext(Dispatchers.IO) {
            mealDao.getLastInsertedMeal()
        }
    }

    // Delete last inserted diet goal
    suspend fun deleteLastInsertedDietGoal() {
        withContext(Dispatchers.IO) {
            val lastMeal = mealDao.getLastInsertedMeal()
            lastMeal?.let {
                mealDao.deleteMeal(it.mealId)
            }
        }
    }

}