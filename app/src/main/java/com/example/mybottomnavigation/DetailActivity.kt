package com.example.mybottomnavigation

import android.content.Intent
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Html
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.example.mybottomnavigation.data.local.entity.EventEntity
import com.example.mybottomnavigation.data.remote.response.ListEventsItem
import com.example.mybottomnavigation.databinding.ActivityEventDetailBinding
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale

class DetailActivity : AppCompatActivity() {

    companion object {
        const val EVENT_DETAIL = "Event_Detail"
    }

    private lateinit var binding: ActivityEventDetailBinding  // Replace with your generated binding class

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityEventDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val actionBar = supportActionBar
        actionBar?.title = "Event Detail"

        val event = if (Build.VERSION.SDK_INT >= 33) {
            intent.getParcelableExtra<EventEntity>("EVENT_DETAIL", EventEntity::class.java)
        } else {
            @Suppress("DEPRECATION")
            intent.getParcelableExtra<EventEntity>("EVENT_DETAIL")
        }

        event?.let { eventItem ->
            Glide.with(this)
                .load(eventItem.imageLogo) // Use the URL from the data class
                .placeholder(R.drawable.baseline_image_24) // Optional placeholder
                .error(R.drawable.baseline_broken_image_24) // Optional error image
                .into(binding.imgDetailLogo)

            val timeString = getString(R.string.jam_event)
            val dateString = getString(R.string.tanggal_event)
            val quotaString = getString(R.string.quota_event)
            binding.tvDetailName.text = eventItem.name
            binding.tvDetailOrg.text = eventItem.ownerName

            val formattedTime = eventItem.beginTime?.let { formatDateTime(it) }
            binding.tvDetailTime.text = String.format(timeString, formattedTime?.get("time") ?: "")
            binding.tvDetailDate.text = String.format(dateString, formattedTime?.get("date") ?: "")
            binding.tvDetailQuota.text = String.format(quotaString, (eventItem.quota?.minus(
                eventItem.registrant!!
            )).toString())

            @Suppress("DEPRECATION")
            binding.tvDetailDesc.setText(Html.fromHtml(eventItem.description), TextView.BufferType.SPANNABLE);

            binding.eventButton.setOnClickListener {
                val eventUrl = eventItem.link // Assuming you have a field for the event URL
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(eventUrl))
                startActivity(intent)
            }
        }
    }

    private fun formatDateTime(dateTimeString: String): Map<String, String> {
        // Parse the date-time string
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
        val dateTime = LocalDateTime.parse(dateTimeString, formatter)

        // Format the date and time
        val dateFormatter = DateTimeFormatter.ofPattern("dd MMMM yyyy", Locale("id", "ID"))
        val timeFormatter = DateTimeFormatter.ofPattern("HH:mm")

        val formattedDate = dateTime.format(dateFormatter) // e.g., "06 October 2024"
        val formattedTime = dateTime.format(timeFormatter) // e.g., "08:00"

        // Return a map with keys and values
        return mapOf(
            "date" to formattedDate,
            "time" to formattedTime
        )
    }
}
