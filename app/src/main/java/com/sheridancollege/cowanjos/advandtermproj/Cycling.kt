package com.sheridancollege.cowanjos.advandtermproj

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDate


@Entity(tableName = "cycling")
data class Cycling(
    @PrimaryKey(autoGenerate = true) val cyclingId: Int,
    val accountId: Int,
    val date: LocalDate,
    val rideDuration: Float,
    val rideDistance: Float
)

