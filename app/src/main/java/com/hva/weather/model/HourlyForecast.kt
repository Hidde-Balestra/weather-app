package com.hva.weather.model

data class HourlyForecast(
    val dayId: Int,
    val time: String,
    val TempC: Int,
    val TempF: Int,

    val chanceOfRain: Int,

    var icon: String,
    var drawableResourceId: Int?,
    var tempMeasurement: String?,
)