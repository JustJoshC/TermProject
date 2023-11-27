package com.sheridancollege.cowanjos.advandtermproj

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "accounts")
data class Account(
    @PrimaryKey(autoGenerate = true)
    val userId: Int,
    val username: String,
    val password: String
)


