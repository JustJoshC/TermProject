package com.sheridancollege.cowanjos.advandtermproj

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData


class AccountRepository(private val accountDao: AccountDao) {
    fun getAccount(userId: Int): LiveData<Account> {
        return accountDao.getAccount(userId)
    }

    fun getAllAccounts(): LiveData<List<Account>> {
        return accountDao.getAllAccounts()
    }

    suspend fun insertAccount(account: Account): LiveData<Long> {
        val accountId = accountDao.insertAccount(account)
        return MutableLiveData(accountId)
    }


    suspend fun updateAccount(userId: Int, username: String, password: String) {
        accountDao.updateAccount(userId, username, password)
    }

    // Method to retrieve userId based on username
    suspend fun getUserIdByUsername(username: String): Int? {
        return accountDao.getUserIdByUsername(username)
    }
}

