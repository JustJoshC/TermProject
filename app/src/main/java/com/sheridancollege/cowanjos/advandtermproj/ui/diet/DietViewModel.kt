package com.sheridancollege.cowanjos.advandtermproj.ui.diet

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class DietViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "Diet Page"
    }
    val text: LiveData<String> = _text
}