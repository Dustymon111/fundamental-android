package com.example.mybottomnavigation.helper

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.mybottomnavigation.data.EventRepository
import com.example.mybottomnavigation.di.Injection
import com.example.mybottomnavigation.ui.home.HomeViewModel

class ViewModelFactory(private val eventRepository: EventRepository, private val pref: SettingPreferences) :
    ViewModelProvider.NewInstanceFactory() {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(HomeViewModel::class.java)) {
            return HomeViewModel(eventRepository, pref) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
    }

    companion object {
        @Volatile
        private var instance: ViewModelFactory? = null
        fun getInstance(context: Context): ViewModelFactory =
            instance ?: synchronized(this) {
                instance ?: ViewModelFactory(Injection.provideRepository(context),SettingPreferences.getInstance((context.applicationContext as MyApplication).getDataStore()) )
            }.also { instance = it }
    }
}