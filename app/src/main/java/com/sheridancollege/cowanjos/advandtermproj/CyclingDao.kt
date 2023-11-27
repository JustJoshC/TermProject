package com.sheridancollege.cowanjos.advandtermproj

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface CyclingDao {
    @Insert
    suspend fun insertCycling(cycling: Cycling): Long

    @Query("SELECT * FROM cycling WHERE cyclingId = :cyclingId")
    suspend fun getCycling(cyclingId: Int): Cycling

    @Query("SELECT * FROM cycling WHERE accountId = :accountId")
    suspend fun getCyclingByAccountId(accountId: Int): List<Cycling>

    @Query("UPDATE cycling SET date = :date, rideDuration = :rideDuration, rideDistance = :rideDistance WHERE cyclingId = :cyclingId")
    suspend fun updateCycling(cyclingId: Int, date: String, rideDuration: String, rideDistance: Float)

    @Query("DELETE FROM cycling WHERE cyclingId = :cyclingId")
    suspend fun deleteCycling(cyclingId: Int)

    @Query("DELETE FROM cycling WHERE accountId = :accountId")
    suspend fun deleteAllCyclingForAccount(accountId: Int)
}
