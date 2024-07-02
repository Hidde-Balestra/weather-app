package com.hva.weather.viewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.hva.weather.data.LocationRepository
import com.hva.weather.model.Locations
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class LocationViewModel(application: Application) : AndroidViewModel(application) {
    private val locationRepository = LocationRepository(application.applicationContext)
    private val mainScope = CoroutineScope(Dispatchers.Main)

    val locations: LiveData<List<Locations>> = locationRepository.getAllLocations()
    private val error = MutableLiveData<String>()
    private val success = MutableLiveData<Boolean>()

    fun createLocation(location: Locations) {
        if(isNoteValid(location)) {
            mainScope.launch {
                locationRepository.insertLocation(location)
                success.value = true
            }
        }
    }

    private fun isNoteValid(location: Locations): Boolean {
        return when {
            location.city.isBlank() -> {
                error.value = "city must not be empty"
                false
            }
            else -> true
        }
    }

    fun deleteLocation(location: Locations) {
        mainScope.launch {
            locationRepository.deleteLocation(location)
        }
    }
}