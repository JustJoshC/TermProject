package com.sheridancollege.cowanjos.advandtermproj

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "diets")

data class Diet(
    @PrimaryKey(autoGenerate = true) val dietId: Int,
    val accountId: String,
    val targetCalories: Int,
    val currentCalories: Int
)

