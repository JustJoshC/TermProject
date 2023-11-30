package com.sheridancollege.cowanjos.advandtermproj

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDate


@Entity(tableName = "cycling")
data class Cycling(
    @PrimaryKey(autoGenerate = true) val cyclingId: Int? = null,
    val accountId: String?,
    val date: LocalDate?,
    val rideDuration: String?,
   @ColumnInfo(name = "ride_distance (km)") val rideDistance: String?
)

