package com.sheridancollege.cowanjos.advandtermproj.ui.workouts

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class WorkoutViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "Workout Page"
    }
    val text: LiveData<String> = _text
}