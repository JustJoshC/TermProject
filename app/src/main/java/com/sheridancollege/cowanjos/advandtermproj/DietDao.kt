package com.sheridancollege.cowanjos.advandtermproj

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface DietDao {
    @Insert
    fun insertDiet(diet: Diet): Long

    @Query("SELECT * FROM diets WHERE dietId = :dietId")
    fun getDiet(dietId: Int): Diet

    @Query("SELECT * FROM diets WHERE accountId = :accountId")
    fun getDietsByAccountId(accountId: Int): List<Diet>

    @Query("UPDATE diets SET targetCalories = :targetCalories, currentCalories = :currentCalories WHERE dietId = :dietId")
    fun updateDiet(dietId: Int, targetCalories: Int, currentCalories: Int)

    @Query("DELETE FROM diets WHERE dietId = :dietId")
    fun deleteDiet(dietId: Int)
}
