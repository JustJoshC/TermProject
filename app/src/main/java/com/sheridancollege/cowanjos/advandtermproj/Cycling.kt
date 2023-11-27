package com.sheridancollege.cowanjos.advandtermproj

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import java.time.LocalDate


@Entity(tableName = "cycling",
    foreignKeys = [ForeignKey(
        entity = Account::class,
        parentColumns = arrayOf("userId"),
        childColumns = arrayOf("accountId"),
        onDelete = ForeignKey.CASCADE)]
)
data class Cycling(
    @PrimaryKey(autoGenerate = true) val cyclingId: Int,
    val accountId: Int,
    val date: LocalDate,
    val rideDuration: Float,
    val rideDistance: Float
)

