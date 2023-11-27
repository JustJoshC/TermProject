package com.sheridancollege.cowanjos.advandtermproj

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDate

@Entity(tableName = "free_weights")

data class FreeWeights(
    @PrimaryKey(autoGenerate = true) val freeWeightsId: Int,
    val accountId: String,
    val date: LocalDate,
    val muscleGroup: String,
    val workoutDuration: String
)
