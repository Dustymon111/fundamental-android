package com.example.mybottomnavigation.data.local.room

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.mybottomnavigation.data.local.entity.EventEntity



@Dao
interface EventDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertEvent(event: List<EventEntity>)

    @Update
    fun update(event: EventEntity)

    @Query("DELETE FROM evententity WHERE isFavorite = 0")
    fun deleteAll()

    @Query("SELECT * FROM evententity WHERE isFavorite = 1")
    fun getFavoriteEvents(): LiveData<List<EventEntity>>

    @Query("SELECT * FROM evententity ORDER BY id ASC")
    fun getAllEvents(): LiveData<List<EventEntity>>

    @Query("SELECT EXISTS(SELECT * FROM evententity WHERE name = :name AND isFavorite = 1)")
    fun isEventFavorite(name: String): Boolean
}
