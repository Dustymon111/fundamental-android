package com.example.mybottomnavigation.helper

import androidx.recyclerview.widget.DiffUtil
import com.example.mybottomnavigation.data.local.entity.EventEntity

class FavoriteEventDiffCallbackDiffCallback(private val oldFavoriteEventList: List<EventEntity>, private val newFavoriteEventList: List<EventEntity>) : DiffUtil.Callback() {
    override fun getOldListSize(): Int = oldFavoriteEventList.size
    override fun getNewListSize(): Int = newFavoriteEventList.size
    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldFavoriteEventList[oldItemPosition].id == newFavoriteEventList[newItemPosition].id
    }
    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldFavoriteEvent = oldFavoriteEventList[oldItemPosition]
        val newFavoriteEvent = newFavoriteEventList[newItemPosition]
        return oldFavoriteEvent.name == newFavoriteEvent.name && oldFavoriteEvent.description == newFavoriteEvent.description
    }
}