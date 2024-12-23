package com.example.mybottomnavigation.ui.adapter

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
import com.example.mybottomnavigation.databinding.VerticalItemCardBinding

class VerticalViewAdapter(private val onFavoriteClick: (EventEntity) -> Unit) : ListAdapter<EventEntity, VerticalViewAdapter.ListViewHolder>(DIFF_CALLBACK) {
    private lateinit var onItemClickCallback: OnItemClickCallback
    private var fullList = listOf<EventEntity>()

    fun setFullList(list: List<EventEntity>) {
        fullList = list
        submitList(fullList)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val binding = VerticalItemCardBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ListViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val event = getItem(position)

        if (!event.imageLogo.isNullOrEmpty()) {
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
        ivFavorite.setImageDrawable(
            ContextCompat.getDrawable(
                ivFavorite.context,
                if (event.isFavorite == true) R.drawable.baseline_favorite_24_favorite else R.drawable.baseline_favorite_24_not_favorite
            )
        )

        ivFavorite.setOnClickListener {
            onFavoriteClick(event)
            notifyItemChanged(holder.adapterPosition)
        }
    }

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    interface OnItemClickCallback {
        fun onItemClicked(data: EventEntity)
    }

    fun filter(query: String) {
        val filteredList = if (query.isEmpty()) {
            fullList
        } else {
            fullList.filter { it.name?.contains(query, ignoreCase = true) == true }
        }
        submitList(filteredList)
    }

    class ListViewHolder(var binding: VerticalItemCardBinding) : RecyclerView.ViewHolder(binding.root)

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<EventEntity>() {
            override fun areItemsTheSame(oldItem: EventEntity, newItem: EventEntity): Boolean {
                return oldItem.id == newItem.id // Assuming id is unique
            }

            override fun areContentsTheSame(oldItem: EventEntity, newItem: EventEntity): Boolean {
                return oldItem == newItem
            }
        }
    }
}
