package com.hva.weather.viewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.hva.weather.model.*
import com.loopj.android.http.AsyncHttpClient
import com.loopj.android.http.JsonHttpResponseHandler
import cz.msebera.android.httpclient.Header
import org.json.JSONException
import org.json.JSONObject

class WeatherViewModel(application: Application) : AndroidViewModel(application) {
    private val appKey = "f1b4809122cb497a953111452222012"
    private lateinit var geoLocationString: String
    private var dayAmount = "3"
    private val airQuality = "no"
    private val alerts = "no"

    var onlyCityCheck: Boolean = false
    var isFistLocation: Boolean = false

    var dayId : Int = -1
    var errorCode: Int = -1
    var geoLocation = MutableLiveData<GeoLocation>()
    var currentWeather = MutableLiveData<CurrentWeather?>()
    var dailyForecast = MutableLiveData<DailyForecast?>()
    var hourlyForecast : MutableList<HourlyForecast> = ArrayList()
    var currentLocation = MutableLiveData<Locations?>()
    var firstLocation = MutableLiveData<Locations>()

    fun weatherFromGeoLocation(latitude: Double, longitude: Double){
        isFistLocation = true
        geoLocation.value = GeoLocation(true, latitude, longitude)
        geoLocationString = "${latitude},${longitude}"
        getWeather()
    }

    fun weatherFromCityLocation(city: String){
        currentWeather.value = null
        dailyForecast.value = null
        hourlyForecast.clear()
        currentLocation.value = null
        dayAmount = "3"
        geoLocationString = city
        getWeather()
    }

    fun checkIfCityExist(city: String){
        geoLocationString = city
        currentLocation.value = null
        onlyCityCheck = true
        dayAmount = "1"
        getWeather()
    }

    private fun getWeather() {
        val url = "https://api.weatherapi.com/v1/forecast.json?key=${appKey}&q=${geoLocationString}&days=${dayAmount}&aqi=${airQuality}&alerts=${alerts}"
        val client = AsyncHttpClient()
        client.get(url, object : JsonHttpResponseHandler() {
            override fun onSuccess(statusCode: Int, headers: Array<Header?>?, response: JSONObject?) {
                try {
                    errorCode = -1
                    if (response != null) {
                        makeLocation(response)
                        if (!onlyCityCheck){
                            makeCurrentWeather(response)
                            makeDailyForecast(response)
                        } else {
                            onlyCityCheck = false
                        }
                    }

                } catch (e: JSONException) {
                    //exception
                    e.printStackTrace()
                }
            }

            override fun onFailure(
                statusCode: Int,
                headers: Array<Header?>?,
                throwable: Throwable?,
                errorResponse: JSONObject?
            ) {
                errorCode = statusCode
                currentWeather.value = null
                currentLocation.value = null
                println("error $statusCode")
            }
        })
    }

    private fun makeLocation(response: JSONObject){
        val locationArray = response.getJSONObject("location") ?: return
        val city = locationArray.getString("name")
        if (isFistLocation){
            isFistLocation = false
            firstLocation.value = Locations(city, true)
        }
        currentLocation.value = Locations(city, false)
    }
    private fun makeCurrentWeather(response: JSONObject){
        val currentArray = response.getJSONObject("current") ?: return

        val updateTime = currentArray.getString("last_updated")

        val tempC = currentArray.getInt("temp_c")
        val tempF = currentArray.getInt("temp_f")
        val feelsLikeC = currentArray.getInt("feelslike_c")
        val feelsLikeF = currentArray.getInt("feelslike_f")

        val windKph = currentArray.getInt("wind_kph")
        val windMph = currentArray.getInt("wind_mph")
        val windDir = currentArray.getString("wind_dir")

        val pressure = currentArray.getInt("pressure_mb")
        val humidity = currentArray.getInt("humidity")

        val visKm = currentArray.getInt("vis_km")
        val visMiles = currentArray.getInt("vis_miles")

        val uv = currentArray.getInt("uv")

        val weatherStatus = currentArray.getJSONObject("condition")
        val status = weatherStatus.getString("text")
        val icon = weatherStatus.getString("code")
        currentWeather.value = CurrentWeather(updateTime, tempC, tempF, feelsLikeC, feelsLikeF, windKph, windMph, windDir, pressure, humidity, visKm, visMiles, uv, status, icon)
    }
    private fun makeDailyForecast(response: JSONObject){
        val forecastArray = response.getJSONObject("forecast") ?: return
        val forecastDayArray = forecastArray.getJSONArray("forecastday") ?: return

        for (i in 0 until forecastDayArray.length()) {
            val forecastDay = forecastDayArray.getJSONObject(i)
            val forecastObj = forecastDay.getJSONObject("day")

            val date = forecastDay.getString("date")
            val minTempC = forecastObj.getInt("mintemp_c")
            val minTempF = forecastObj.getInt("mintemp_f")
            val maxTempC = forecastObj.getInt("maxtemp_c")
            val maxTempF = forecastObj.getInt("maxtemp_f")
            val chanceOfRain = forecastObj.getInt("daily_chance_of_rain")

            val weatherStatus = forecastObj.getJSONObject("condition")
            val icon = weatherStatus.getString("code")

            dailyForecast.value = DailyForecast(i,date,minTempC,minTempF,maxTempC,maxTempF, chanceOfRain, icon, null, null)
            makeHourlyForecast(forecastDay, i)
        }
    }
    private fun makeHourlyForecast(response: JSONObject, dayId: Int){
        val forecastHourArray = response.getJSONArray("hour")

        for (i in 0 until forecastHourArray.length()) {
            val forecastHour = forecastHourArray.getJSONObject(i)

            val time = forecastHour.getString("time")
            val tempC = forecastHour.getInt("temp_c")
            val tempF = forecastHour.getInt("temp_f")
            val chanceOfRain = forecastHour.getInt("chance_of_rain")

            val weatherStatus = forecastHour.getJSONObject("condition")
            val icon = weatherStatus.getString("code")

            val weatherData = HourlyForecast(dayId ,time, tempC, tempF, chanceOfRain, icon, null, null)

            hourlyForecast.add(weatherData)
        }
    }
}