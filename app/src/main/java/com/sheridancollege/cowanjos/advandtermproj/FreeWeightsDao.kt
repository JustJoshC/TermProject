package com.sheridancollege.cowanjos.advandtermproj

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import java.time.LocalDate

@Dao
interface FreeWeightsDao {
    @Insert
    suspend fun insertFreeWeights(freeWeights: FreeWeights): Long

    @Query("SELECT * FROM free_weights WHERE freeWeightsId = :freeWeightsId")
    suspend fun getFreeWeights(freeWeightsId: Int): FreeWeights

    @Query("SELECT * FROM free_weights")
    fun getAllFreeWeights(): LiveData<List<FreeWeights>>

    @Query("UPDATE free_weights SET date = :date, muscleGroup = :muscleGroup, workoutDuration = :workoutDuration WHERE freeWeightsId = :freeWeightsId")
    suspend fun updateFreeWeights(freeWeightsId: Int, date: String, muscleGroup: String, workoutDuration: String)

    @Query("DELETE FROM free_weights WHERE freeWeightsId = :freeWeightsId")
    suspend fun deleteFreeWeights(freeWeightsId: Int)

    @Query("DELETE FROM free_weights WHERE accountId = :accountId")
    suspend fun deleteAllFreeWeightsForAccount(accountId: String)

    /**
     * Checks if a workout with the specified muscle group and date already exists in the database,
     * excluding a specific workout ID.
     * @param muscleGroup The muscle group of the workout.
     * @param date The date of the workout.
     * @param excludeId The ID of the workout to exclude from the check.
     * @return Boolean indicating whether a duplicate workout exists.
     */
    @Query("SELECT COUNT(*) FROM free_weights WHERE muscleGroup = :muscleGroup AND date = :date AND freeWeightsId != :excludeId")
    suspend fun checkForExistingWorkout(muscleGroup: String, date: String, excludeId: Int?): Int
}
