package com.sheridancollege.cowanjos.advandtermproj

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import java.time.LocalTime


@Entity(tableName = "meals",
    foreignKeys = [ForeignKey(
        entity = Diet::class,
        parentColumns = arrayOf("dietId"),
        childColumns = arrayOf("dietId"),
        onDelete = ForeignKey.CASCADE)]
)
data class Meal(
    @PrimaryKey(autoGenerate = true) val mealId: Int,
    val dietId: Int,
    val mealTime: LocalTime,
    val mealDescription: String,
    val mealCalories: Int
)

