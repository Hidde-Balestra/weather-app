package com.hva.weather.data

import androidx.lifecycle.LiveData
import androidx.room.*
import com.hva.weather.model.Locations

@Dao
interface LocationDao {

    @Query("SELECT * FROM locationListTable")
    fun getAllLocations(): LiveData<List<Locations>>

    @Query("SELECT * FROM locationListTable")
    fun getLocation(): LiveData<Locations>

    @Insert
    suspend fun insertLocation(location: Locations)

    @Delete
    suspend fun deleteLocation(location: Locations)

    @Update
    suspend fun updateLocation(location: Locations)

}
