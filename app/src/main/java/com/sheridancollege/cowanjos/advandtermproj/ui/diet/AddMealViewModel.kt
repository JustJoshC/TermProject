package com.sheridancollege.cowanjos.advandtermproj.ui.diet

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class AddMealViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "Add Meal Page"
    }
    val text: LiveData<String> = _text
}