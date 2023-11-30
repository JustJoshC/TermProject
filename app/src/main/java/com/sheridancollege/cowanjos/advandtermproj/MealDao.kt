package com.sheridancollege.cowanjos.advandtermproj

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface MealDao {
    @Insert
    suspend fun insertMeal(meal: Meal): Long

    @Query("SELECT * FROM meals WHERE mealId = :mealId")
    suspend fun getMeal(mealId: Int): Meal

    @Query("SELECT * FROM meals WHERE dietId = :dietId")
    suspend fun getMealsByDietId(dietId: Int): List<Meal>

    @Query("UPDATE meals SET mealDescription = :description, mealCalories = :calories WHERE mealId = :mealId")
    suspend fun updateMeal(mealId: Int, description: String, calories: Int)

    @Query("DELETE FROM meals WHERE mealId = :mealId")
    suspend fun deleteMeal(mealId: Int)

    @Query("SELECT * FROM meals")
    fun getAllMeals(): LiveData<List<Meal>>
}
