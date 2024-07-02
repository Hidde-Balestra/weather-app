package com.hva.weather.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "locationListTable")
data class Locations(
    val city: String,
    val isCurrentLocation: Boolean,

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    var id: Long? = null,
)