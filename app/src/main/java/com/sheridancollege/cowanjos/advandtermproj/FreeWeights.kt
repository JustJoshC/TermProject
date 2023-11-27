package com.sheridancollege.cowanjos.advandtermproj

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import java.time.LocalDate

@Entity(tableName = "free_weights",
    foreignKeys = [ForeignKey(
        entity = Account::class,
        parentColumns = arrayOf("userId"),
        childColumns = arrayOf("accountId"),
        onDelete = ForeignKey.CASCADE)]
)

data class FreeWeights(
    @PrimaryKey(autoGenerate = true) val freeWeightsId: Int,
    val accountId: Int,
    val date: LocalDate,
    val muscleGroup: String,
    val workoutDuration: String
)
