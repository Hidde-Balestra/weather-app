package com.hva.weather.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "settingsTable")
data class Settings(
    val tempMeasurement: String,
    val distanceMeasurement: String,

    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = "id")
    var id: Long,
)