package com.google.jetfit.presentation.screens.settings

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor() : ViewModel() {

    private val _settingsUIState = MutableStateFlow(SettingsUIState())
    val settingsUIState = _settingsUIState.asStateFlow()

    fun updateSetting(item: SettingsItemUIState) {
        val appSetting = SettingsMenuUIState.AppSettings.entries.find { it.name == item.title }
        val personalSetting = SettingsMenuUIState.PersonalSettings.entries.find { it.name == item.title }

        appSetting?.value = item.value
        personalSetting?.value = item.value

        val updatedAppSettings = _settingsUIState.value.appSettings.second.map { if (it.title == item.title) item else it }
        val updatedPersonalSettings = _settingsUIState.value.personalSettings.second.map { if (it.title == item.title) item else it }

        _settingsUIState.value = _settingsUIState.value.copy(
            appSettings = _settingsUIState.value.appSettings.first to updatedAppSettings,
            personalSettings = _settingsUIState.value.personalSettings.first to updatedPersonalSettings
        )
    }
}