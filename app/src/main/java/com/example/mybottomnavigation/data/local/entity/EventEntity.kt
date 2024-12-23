package com.example.mybottomnavigation.data.local.entity

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Entity(tableName = "evententity")
@Parcelize
data class EventEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    var id: Int = 0,

    @ColumnInfo(name = "imageLogo")
     var imageLogo: String? = null,

    @ColumnInfo(name = "name")
     var name: String? = null,

    @ColumnInfo(name = "description")
     var description: String? = null,

    @ColumnInfo(name = "beginTime")
     var beginTime: String? = null,

    @ColumnInfo(name = "ownerName")
     var ownerName: String? = null,

    @ColumnInfo(name = "registrant")
     var registrant: Int? = null,

    @ColumnInfo(name = "quota")
     var quota: Int? = null,

    @ColumnInfo(name = "link")
     var link: String? = null,

    @ColumnInfo(name = "isFavorite")
     var isFavorite: Boolean? = null,

    @ColumnInfo(name = "isActive")
    var isActive: Boolean? = null,

) : Parcelable
