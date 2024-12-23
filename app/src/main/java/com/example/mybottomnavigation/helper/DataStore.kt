package com.example.mybottomnavigation.helper

import android.app.Application
import android.content.Context
import androidx.datastore.preferences.preferencesDataStore
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences

class MyApplication : Application() {
    // Create a delegate for DataStore
    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

    // Access the DataStore in your application class
    override fun onCreate() {
        super.onCreate()
        // You can access your DataStore here if needed
    }

    // Provide a method to get the DataStore
    fun getDataStore(): DataStore<Preferences> {
        return dataStore
    }
}