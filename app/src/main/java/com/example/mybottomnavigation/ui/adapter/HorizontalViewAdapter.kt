package com.example.mybottomnavigation.ui.adapter

import android.annotation.SuppressLint
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.mybottomnavigation.R
import com.example.mybottomnavigation.data.local.entity.EventEntity
import com.example.mybottomnavigation.databinding.HorizontalItemCardBinding

class HorizontalViewAdapter(private val onFavoriteClick: (EventEntity) -> Unit) : ListAdapter<EventEntity, HorizontalViewAdapter.ListViewHolder>(DIFF_CALLBACK) {
    private lateinit var onItemClickCallback: OnItemClickCallback

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val binding = HorizontalItemCardBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ListViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val event = getItem(position)

        if (event.imageLogo!!.isNotEmpty()) {
            Log.d("Image URL", "Image URL: ${event.imageLogo}")
             Glide.with(holder.itemView.context)
                 .load(event.imageLogo)
                 .apply(RequestOptions.placeholderOf(android.R.drawable.ic_menu_myplaces))
                 .into(holder.binding.imgItemLogo)
        }
        holder.binding.tvItemName.text = event.name
        holder.itemView.setOnClickListener {
            onItemClickCallback.onItemClicked(event)
        }

        val ivFavorite = holder.binding.ivFavorite
        if (event.isFavorite == true) {
            ivFavorite.setImageDrawable(ContextCompat.getDrawable(ivFavorite.context, R.drawable.baseline_favorite_24_favorite))
        } else {
            ivFavorite.setImageDrawable(ContextCompat.getDrawable(ivFavorite.context, R.drawable.baseline_favorite_24_not_favorite))
        }
        ivFavorite.setOnClickListener {
            onFavoriteClick(event)
            notifyItemChanged(holder.adapterPosition)
        }
    }

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    interface OnItemClickCallback {
        fun onItemClicked(data: EventEntity) // Change to ListEventsItem
    }

    class ListViewHolder(var binding: HorizontalItemCardBinding) : RecyclerView.ViewHolder(binding.root)

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<EventEntity>() {
            override fun areItemsTheSame(oldItem: EventEntity, newItem: EventEntity): Boolean {
                return oldItem.id == newItem.id // Assuming id is unique
            }

            @SuppressLint("DiffUtilEquals")
            override fun areContentsTheSame(oldItem: EventEntity, newItem: EventEntity): Boolean {
                return oldItem == newItem
            }
        }
    }
}