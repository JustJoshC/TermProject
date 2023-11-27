package com.sheridancollege.cowanjos.advandtermproj

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(tableName = "diets",
    foreignKeys = [ForeignKey(
        entity = Account::class,
        parentColumns = arrayOf("userId"),
        childColumns = arrayOf("accountId"),
        onDelete = ForeignKey.CASCADE)]
)
data class Diet(
    @PrimaryKey(autoGenerate = true) val dietId: Int,
    val accountId: Int,
    val targetCalories: Int,
    val currentCalories: Int
)

