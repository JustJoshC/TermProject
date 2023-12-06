package com.sheridancollege.cowanjos.advandtermproj.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class HomeViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "Fuel Your Fitness: Track, Train, Transform! \uD83D\uDCAA"
    }
    val text: LiveData<String> = _text
}