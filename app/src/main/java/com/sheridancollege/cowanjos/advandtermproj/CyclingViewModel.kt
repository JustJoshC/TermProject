package com.sheridancollege.cowanjos.advandtermproj

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import java.time.LocalDate

class CyclingViewModel(private val cyclingDao: CyclingDao) : ViewModel() {
    val allCyclingData: LiveData<List<Cycling>> = cyclingDao.getAll().asLiveData()
    val duplicateWorkoutDetected = MutableLiveData<Boolean>()
    fun insertCyclingData(cycling: Cycling) {
        viewModelScope.launch {
            if (!checkForExistingWorkout(cycling.date, cycling.rideDuration, cycling.rideDistance)) {
                cyclingDao.insertCycling(cycling)
            }
            else{
                duplicateWorkoutDetected.value = true
            }
        }
    }

    fun deleteCyclingData(cyclingId: Int?) {
        viewModelScope.launch {
            cyclingDao.deleteCycling(cyclingId)
        }
    }

    fun updateCycling(cycling: Cycling) {
        viewModelScope.launch {
            cyclingDao.updateCycling(cycling)
        }
    }

    suspend fun checkForExistingWorkout(
        date: LocalDate?,
        rideDuration: String?,
        rideDistance: String?
    ): Boolean {
        var count: Int = 0
        viewModelScope.launch {
            count = cyclingDao.checkExistingCycling(date, rideDistance, rideDuration)
        }
        return count > 0
    }
}

class CyclingViewModelFactory(private val cyclingDao: CyclingDao) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CyclingViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return CyclingViewModel(cyclingDao) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}