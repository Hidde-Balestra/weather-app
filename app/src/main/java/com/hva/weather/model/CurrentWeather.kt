package com.hva.weather.model

data class CurrentWeather(
    val updateTime: String,

    val tempC: Int,
    val tempF: Int,
    val feelsLikeC: Int,
    val feelsLikeF: Int,

    val windKph: Int,
    val windMph: Int,
    val windDir: String,

    val pressure: Int,
    val humidity: Int,

    val visKm: Int,
    val visMiles: Int,

    val uv: Int,

    val status: String,
    val icon: String
)