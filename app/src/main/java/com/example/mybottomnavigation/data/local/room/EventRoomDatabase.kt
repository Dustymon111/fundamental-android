package com.example.mybottomnavigation.data.local.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.mybottomnavigation.data.local.entity.EventEntity

@Database(entities = [EventEntity::class], version = 1)
abstract class EventRoomDatabase : RoomDatabase() {
    abstract fun EventDao(): EventDao
    companion object {
        @Volatile
        private var INSTANCE: EventRoomDatabase? = null
        @JvmStatic
        fun getDatabase(context: Context): EventRoomDatabase {
            if (INSTANCE == null) {
                synchronized(EventRoomDatabase::class.java) {
                    INSTANCE = Room.databaseBuilder(context.applicationContext,
                        EventRoomDatabase::class.java, "event_database")
                        .build()
                }
            }
            return INSTANCE as EventRoomDatabase
        }
    }
}