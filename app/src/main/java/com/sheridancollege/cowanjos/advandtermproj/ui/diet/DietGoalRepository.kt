package com.sheridancollege.cowanjos.advandtermproj.ui.diet

import androidx.lifecycle.LiveData
import com.sheridancollege.cowanjos.advandtermproj.Diet
import com.sheridancollege.cowanjos.advandtermproj.DietDao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class DietGoalRepository (private val dietDao: DietDao) {

    val dietGoalList: LiveData<List<Diet>> = dietDao.getAllDiets()

    // insert diet goal
    suspend fun insertDietGoal(diet: Diet){
        withContext(Dispatchers.IO){
            dietDao.insertDiet(diet)
        }

    }

    // delete meal func
    suspend fun getLastInsertedDietGoal(): Diet? {
        return withContext(Dispatchers.IO) {
            dietDao.getLastInsertedDiet()
        }
    }

    // Delete last inserted diet goal
    suspend fun deleteLastInsertedDietGoal() {
        withContext(Dispatchers.IO) {
            val lastDiet = getLastInsertedDietGoal()
            lastDiet?.let {
                dietDao.deleteDiet(it.dietId)
            }
        }
    }




}