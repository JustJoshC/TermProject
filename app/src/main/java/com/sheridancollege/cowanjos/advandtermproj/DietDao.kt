package com.sheridancollege.cowanjos.advandtermproj

import androidx.lifecycle.LiveData
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

    @Query("DELETE FROM diets WHERE dietId = :dietId")
    fun deleteDiet(dietId: Int)

    @Query("SELECT * FROM diets")
    fun getAllDiets(): LiveData<List<Diet>>

    @Query("SELECT * FROM diets ORDER BY dietId DESC LIMIT 1")
    suspend fun getLastInsertedDiet(): Diet?

}
