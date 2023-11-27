package com.sheridancollege.cowanjos.advandtermproj.ui.account

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sheridancollege.cowanjos.advandtermproj.Account
import com.sheridancollege.cowanjos.advandtermproj.AccountRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class AccountViewModel(private val repository: AccountRepository) : ViewModel() {
    private var userId: Int = 0
    val account: LiveData<Account> = repository.getAccount(userId) // Adjust `userId` accordingly
    val allAccounts: LiveData<List<Account>> = repository.getAllAccounts()

    suspend fun updateAccount(userId: Int, username: String, password: String) {
        repository.updateAccount(userId, username, password)
    }

    suspend fun insertAccount(account: Account) {
        repository.insertAccount(account)
    }

    // Method to get userId based on username
    suspend fun getUserIdByUsername(username: String): Int? {
        return repository.getUserIdByUsername(username)
    }

}
