package com.sheridancollege.cowanjos.advandtermproj

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface AccountDao {
    @Insert
    suspend fun insertAccount(account: Account): Long

    @Query("SELECT * FROM accounts WHERE userId = :userId")
    fun getAccount(userId: Int): LiveData<Account>

    @Query("SELECT * FROM accounts")
    fun getAllAccounts(): LiveData<List<Account>>

    @Query("UPDATE accounts SET username = :username, password = :password WHERE userId = :userId")
    fun updateAccount(userId: Int, username: String, password: String)

    @Query("SELECT userId FROM accounts WHERE username = :username")
    suspend fun getUserIdByUsername(username: String): Int?
}

