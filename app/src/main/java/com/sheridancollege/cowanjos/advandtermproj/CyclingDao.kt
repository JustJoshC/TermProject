package com.sheridancollege.cowanjos.advandtermproj

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow
import java.time.Duration
import java.time.LocalDate

@Dao
interface CyclingDao {
    @Insert
    suspend fun insertCycling(cycling: Cycling): Long

    @Query("SELECT * FROM cycling")
   fun getAll(): Flow<List<Cycling>>

    @Update
    suspend fun updateCycling(vararg cycling: Cycling)

    @Query("DELETE FROM cycling WHERE cyclingId = :cyclingId")
    suspend fun deleteCycling(cyclingId: Int?)

    @Query("SELECT COUNT(*) FROM cycling WHERE date=:date AND `ride_distance (km)`=:distance AND rideDuration=:duration")
    suspend fun checkExistingCycling(date: LocalDate?, distance:String?, duration: String?): Int
}
