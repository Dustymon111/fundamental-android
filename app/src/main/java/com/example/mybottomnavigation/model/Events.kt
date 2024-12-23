package com.example.mybottomnavigation.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Events(
    val thumbnail: Int,
    val name: String?,
    val org: String?,
    val time: String?,
    val quota: Int,
    val description: String?
) : Parcelable
