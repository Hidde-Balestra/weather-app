package com.hva.weather.model

data class DailyForecast(
    val id: Int,
    val date: String,
    val minTempC: Int,
    val minTempF: Int,
    val maxTempC: Int,
    val maxTempF: Int,

    val chanceOfRain: Int,

    val icon: String,
    var drawableResourceId: Int?,
    var tempMeasurement: String?,
)