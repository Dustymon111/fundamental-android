package com.example.mybottomnavigation.di

import android.content.Context
import com.example.mybottomnavigation.data.EventRepository
import com.example.mybottomnavigation.data.local.room.EventRoomDatabase
import com.example.mybottomnavigation.data.remote.retrofit.ApiConfig
import com.example.mybottomnavigation.helper.AppExecutors
import com.example.mybottomnavigation.helper.SettingPreferences

object Injection {
    fun provideRepository(context: Context): EventRepository {
        val apiService = ApiConfig.getApiService()
        val database = EventRoomDatabase.getDatabase(context)
        val dao = database.EventDao()
        val appExecutors = AppExecutors()
        return EventRepository.getInstance(apiService, dao, appExecutors)
    }
}