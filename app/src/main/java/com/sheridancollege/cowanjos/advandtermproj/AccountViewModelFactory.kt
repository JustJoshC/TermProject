package com.sheridancollege.cowanjos.advandtermproj

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.sheridancollege.cowanjos.advandtermproj.ui.account.AccountViewModel

class AccountViewModelFactory(private val repository: AccountRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AccountViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return AccountViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
