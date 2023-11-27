package com.sheridancollege.cowanjos.advandtermproj

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface FreeWeightsDao {
    @Insert
    suspend fun insertFreeWeights(freeWeights: FreeWeights): Long

    @Query("SELECT * FROM free_weights WHERE freeWeightsId = :freeWeightsId")
    suspend fun getFreeWeights(freeWeightsId: Int): FreeWeights

    @Query("SELECT * FROM free_weights")
    fun getAllFreeWeights(): LiveData<List<FreeWeights>>

    @Query("SELECT * FROM free_weights WHERE accountId = :accountId")
    suspend fun getFreeWeightsByAccountId(accountId: Int): List<FreeWeights>

    @Query("UPDATE free_weights SET date = :date, muscleGroup = :muscleGroup, workoutDuration = :workoutDuration WHERE freeWeightsId = :freeWeightsId")
    suspend fun updateFreeWeights(freeWeightsId: Int, date: String, muscleGroup: String, workoutDuration: String)

    @Query("DELETE FROM free_weights WHERE freeWeightsId = :freeWeightsId")
    suspend fun deleteFreeWeights(freeWeightsId: Int)

    @Query("DELETE FROM free_weights WHERE accountId = :accountId")
    suspend fun deleteAllFreeWeightsForAccount(accountId: Int)
}
