package com.google.jetfit.presentation.screens.settings

import com.google.jetfit.R

data class SettingsUIState(
    val settingsMenuUIState: SettingsMenuUIState = SettingsMenuUIState(),
    var appSettings: Pair<Int, List<SettingsItemUIState>> = Pair(
        R.string.app_settings,
        listOf(
            SettingsItemUIState(
                title = SettingsMenuUIState.AppSettings.UnitsPreference.name,
                value = SettingsMenuUIState.AppSettings.UnitsPreference.value,
                possibleValues = listOf("Imperial", "Metric")
            ),
            SettingsItemUIState(
                title = SettingsMenuUIState.AppSettings.Language.name,
                value = SettingsMenuUIState.AppSettings.Language.value,
                possibleValues = listOf("English (US)", "Spanish", "French")
            ),
            SettingsItemUIState(
                title = SettingsMenuUIState.AppSettings.VideoResolution.name,
                value = SettingsMenuUIState.AppSettings.VideoResolution.value,
                possibleValues = listOf("Automatic up to 4k", "1080p", "720p")
            ),
            SettingsItemUIState(
                title = SettingsMenuUIState.AppSettings.Subtitles.name,
                value = SettingsMenuUIState.AppSettings.Subtitles.value,
                possibleValues = listOf("On", "Off")
            )
        )
    ),
    var personalSettings: Pair<Int, List<SettingsItemUIState>> = Pair(
        R.string.personal_settings,
        listOf(
            SettingsItemUIState(
                title = SettingsMenuUIState.PersonalSettings.AccountSettings.name,
                value = SettingsMenuUIState.PersonalSettings.AccountSettings.value,
                possibleValues = listOf("Olivia", "Alex", "Sophia")
            ),
            SettingsItemUIState(
                title = SettingsMenuUIState.PersonalSettings.JetFitPremium.name,
                value = SettingsMenuUIState.PersonalSettings.JetFitPremium.value,
                possibleValues = listOf("3 month", "6 month", "12 month")
            ),
            SettingsItemUIState(
                title = SettingsMenuUIState.PersonalSettings.AboutJetFit.name,
                value = SettingsMenuUIState.PersonalSettings.AboutJetFit.value
            )
        )
    )
)
data class SettingsItemUIState(
    val title: String,
    val value: String,
    val possibleValues: List<String> = emptyList(),
)

data class SettingsMenuUIState(
    val appSettings: AppSettings = AppSettings.UnitsPreference,
    val personalSettings: PersonalSettings = PersonalSettings.AccountSettings
) {
    enum class AppSettings(var value: String) {
        UnitsPreference("Imperial"), Language("English (US)"), VideoResolution("Automatic up to 4k"), Subtitles(
            "On"
        )
    }

    enum class PersonalSettings(var value: String) {
        AccountSettings("Olivia"), JetFitPremium("3 month"), AboutJetFit("")
    }
}
