package com.hva.weather.data

import android.content.Context
import androidx.lifecycle.LiveData
import com.hva.weather.model.Locations

class LocationRepository(context: Context) {
    private var locationDao: LocationDao

    init {
        val locationDatabase = DatabaseRoom.getDatabase(context)
        locationDao = locationDatabase!!.locationDao()
    }

    fun getAllLocations(): LiveData<List<Locations>> {
        return locationDao.getAllLocations()
    }

    fun getLocation() : LiveData<Locations> {
        return locationDao.getLocation()
    }

    suspend fun insertLocation(location: Locations) {
        locationDao.insertLocation(location)
    }

    suspend fun deleteLocation(location: Locations) {
        locationDao.deleteLocation(location)
    }

    suspend fun updateLocation(location: Locations) {
        locationDao.updateLocation(location)
    }
}
