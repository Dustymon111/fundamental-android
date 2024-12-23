package com.example.mybottomnavigation.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.mybottomnavigation.data.EventRepository
import com.example.mybottomnavigation.data.local.entity.EventEntity
import com.example.mybottomnavigation.helper.SettingPreferences
import kotlinx.coroutines.launch

class HomeViewModel(
    private val eventRepository: EventRepository,
    private val pref: SettingPreferences
) : ViewModel() {

    fun getUpcomingEvents() = eventRepository.getUpcomingEvents()

    fun getFinishedEvents() = eventRepository.getFinishedEvents()

    fun getFavoriteEvents() = eventRepository.getFavoriteEvents()

    fun setFavorite(event: EventEntity) {
        eventRepository.setFavoriteEvent(event, true)
    }

    fun removeFavorite(event: EventEntity) {
        eventRepository.setFavoriteEvent(event, false)
    }

    fun getThemeSettings(): LiveData<Boolean> {
        return pref.getThemeSetting().asLiveData()
    }

    fun saveThemeSetting(isDarkModeActive: Boolean) {
        viewModelScope.launch {
            pref.saveThemeSetting(isDarkModeActive)
        }
    }

    fun setDailyReminder(active: Boolean) {
        viewModelScope.launch {
            pref.setDailyReminderSetting(active)
        }
    }

    fun getDailyReminderSetting(): LiveData<Boolean> {
        return pref.getDailyReminderSetting().asLiveData() // Convert Flow to LiveData
    }
}
