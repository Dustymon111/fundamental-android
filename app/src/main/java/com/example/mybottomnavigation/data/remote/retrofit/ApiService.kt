package com.example.mybottomnavigation.data.remote.retrofit

import com.example.mybottomnavigation.data.remote.response.EventResponse
import retrofit2.Call
import retrofit2.http.*

interface ApiService {
    @GET("events")
    fun getEvents(
        @Query("active") active: String
    ): Call<EventResponse>
}