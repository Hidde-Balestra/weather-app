package com.hva.weather.viewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.hva.weather.model.Settings

class SettingsViewModel(application: Application) : AndroidViewModel(application) {
    val settings = MutableLiveData<Settings>()

    fun updateSettings(setting: Settings) {
        settings.value = setting
    }
}