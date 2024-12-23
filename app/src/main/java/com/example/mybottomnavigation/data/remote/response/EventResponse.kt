package com.example.mybottomnavigation.data.remote.response

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

data class EventResponse(

	@field:SerializedName("listEvents")
	val listEvents: List<ListEventsItem>,

	@field:SerializedName("error")
	val error: Boolean,

	@field:SerializedName("message")
	val message: String
)

@Parcelize
data class ListEventsItem(

	@field:SerializedName("imageLogo")
	val imageLogo: String,

	@field:SerializedName("quota")
	val quota: Int,

	@field:SerializedName("registrants")
	val registrant: Int,

	@field:SerializedName("name")
	val name: String,

	@field:SerializedName("ownerName")
	val ownerName: String,

	@field:SerializedName("link")
	val link: String,

	@field:SerializedName("description")
	val description: String,

	@field:SerializedName("id")
	val id: Int,

	@field:SerializedName("beginTime")
	val beginTime: String,

	@field:SerializedName("isFavorite")
	val isFavorite: Boolean,
) : Parcelable
