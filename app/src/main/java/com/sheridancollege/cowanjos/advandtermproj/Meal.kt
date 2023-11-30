package com.sheridancollege.cowanjos.advandtermproj

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import java.time.LocalTime


@Entity(tableName = "meals")

data class Meal(
    @PrimaryKey(autoGenerate = true) val mealId: Int,
    val dietId: Int,
    val accountId: String?,
    val mealTime: String,
    val mealDescription: String,
    val mealCalories: Int
)

