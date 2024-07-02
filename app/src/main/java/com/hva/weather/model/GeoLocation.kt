package com.hva.weather.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "geoLocationListTable")
data class GeoLocation(
    val isEnabled: Boolean,
    val latitude: Double?,
    val Longitude: Double?,

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    var id: Long? = null,
)