package com.example.mybottomnavigation.data

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import com.example.mybottomnavigation.data.local.entity.EventEntity
import com.example.mybottomnavigation.data.local.room.EventDao
import com.example.mybottomnavigation.data.remote.response.EventResponse
import com.example.mybottomnavigation.data.remote.retrofit.ApiService
import com.example.mybottomnavigation.helper.AppExecutors
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

    class EventRepository private constructor(
        private val apiService: ApiService,
        private val eventDao: EventDao,
        private val appExecutors: AppExecutors
    ) {
        private val finishedEventResult = MediatorLiveData<Result<List<EventEntity>>>()
        private val upcomingEventResult = MediatorLiveData<Result<List<EventEntity>>>()


        fun getFinishedEvents(): LiveData<Result<List<EventEntity>>> {
            finishedEventResult.value = Result.Loading
            val client = apiService.getEvents("0")
            client.enqueue(object : Callback<EventResponse> {
                override fun onResponse(call: Call<EventResponse>, response: Response<EventResponse>) {
                    if (response.isSuccessful) {
                        val articles = response.body()?.listEvents
                        val newsList = ArrayList<EventEntity>()
                        appExecutors.diskIO.execute {
                            articles?.forEach { article ->
                                val isFavorite = eventDao.isEventFavorite(article.name)
                                val news = EventEntity(
                                    article.id,
                                    article.imageLogo,
                                    article.name,
                                    article.description,
                                    article.beginTime,
                                    article.ownerName,
                                    article.registrant,
                                    article.quota,
                                    article.link,
                                    isFavorite,
                                    false
                                )
                                newsList.add(news)
                            }
                            eventDao.insertEvent(newsList)
                            Log.d("Query All", "Query All: ${eventDao.getAllEvents().value}")
                        }
                    }
                }

                override fun onFailure(call: Call<EventResponse>, t: Throwable) {
                    finishedEventResult.value = Result.Error(t.message.toString())
                }
            })
            val localData = eventDao.getAllEvents()
            finishedEventResult.addSource(localData) { newData: List<EventEntity> ->
                finishedEventResult.value = Result.Success(newData)
            }
            return finishedEventResult
        }

        fun getUpcomingEvents(): LiveData<Result<List<EventEntity>>> {
            upcomingEventResult.value = Result.Loading
            val client = apiService.getEvents("1")
            client.enqueue(object : Callback<EventResponse> {
                override fun onResponse(call: Call<EventResponse>, response: Response<EventResponse>) {
                    if (response.isSuccessful) {
                        val articles = response.body()?.listEvents
                        val newsList = ArrayList<EventEntity>()
                        appExecutors.diskIO.execute {
                            articles?.forEach { article ->
                                val isFavorite = eventDao.isEventFavorite(article.name)
                                val news = EventEntity(
                                    article.id,
                                    article.imageLogo,
                                    article.name,
                                    article.description,
                                    article.beginTime,
                                    article.ownerName,
                                    article.registrant,
                                    article.quota,
                                    article.link,
                                    isFavorite,
                                    true
                                )
                                newsList.add(news)
                            }
                            eventDao.insertEvent(newsList)
                            Log.d("Query All", "Query All: ${eventDao.getAllEvents().value}")
                        }
                    }
                }

                override fun onFailure(call: Call<EventResponse>, t: Throwable) {
                    upcomingEventResult.value = Result.Error(t.message.toString())
                }
            })
            val localData = eventDao.getAllEvents()
            upcomingEventResult.addSource(localData) { newData: List<EventEntity> ->
                upcomingEventResult.value = Result.Success(newData)
            }
            return upcomingEventResult
        }

    fun getFavoriteEvents(): LiveData<List<EventEntity>> {
        return eventDao.getFavoriteEvents()
    }
    fun setFavoriteEvent(event: EventEntity, favoriteState: Boolean) {
        appExecutors.diskIO.execute {
                    event.isFavorite = favoriteState
                    eventDao.update(event)
        }
    }

    companion object {
        @Volatile
        private var instance: EventRepository? = null
        fun getInstance(
            apiService: ApiService,
            eventDao: EventDao,
            appExecutors: AppExecutors
        ): EventRepository =
            instance ?: synchronized(this) {
                instance ?: EventRepository(apiService, eventDao, appExecutors)
            }.also { instance = it }
    }
}