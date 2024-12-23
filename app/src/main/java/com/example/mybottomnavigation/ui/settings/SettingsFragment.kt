package com.example.mybottomnavigation.ui.settings

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CompoundButton
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.app.NotificationCompat
import androidx.lifecycle.ViewModelProvider
import androidx.work.Data
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.example.mybottomnavigation.R
import com.example.mybottomnavigation.data.EventRepository
import com.example.mybottomnavigation.data.Result
import com.example.mybottomnavigation.data.local.room.EventDao
import com.example.mybottomnavigation.data.local.room.EventRoomDatabase
import com.example.mybottomnavigation.databinding.FragmentSettingsBinding
import com.example.mybottomnavigation.di.Injection
import com.example.mybottomnavigation.helper.DailyNotificationWorker
import com.example.mybottomnavigation.helper.MyApplication
import com.example.mybottomnavigation.helper.SettingPreferences
import com.example.mybottomnavigation.helper.ViewModelFactory
import com.example.mybottomnavigation.ui.home.HomeViewModel
import com.google.android.material.switchmaterial.SwitchMaterial
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Calendar
import java.util.Locale
import java.util.concurrent.TimeUnit


class SettingsFragment : Fragment() {

    private var _binding: FragmentSettingsBinding? = null
    private val binding get() = _binding!!

    private lateinit var eventDao: EventDao

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSettingsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val switchTheme = binding.switchTheme
        val dailyReminder = binding.dailyReminder

        eventDao = EventRoomDatabase.getDatabase(requireContext()).EventDao()

        val pref = SettingPreferences.getInstance((context?.applicationContext as MyApplication).getDataStore())
        val mainViewModel = ViewModelProvider(this, ViewModelFactory(Injection.provideRepository(context as Context), pref)).get(
            HomeViewModel::class.java
        )

        mainViewModel.getDailyReminderSetting().observe(viewLifecycleOwner) { isEnabled ->
            binding.dailyReminder.isChecked = isEnabled
        }

        mainViewModel.getThemeSettings().observe(viewLifecycleOwner) { isDarkModeActive: Boolean ->
            if (isDarkModeActive) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                switchTheme.isChecked = true
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                switchTheme.isChecked = false
            }
        }

        switchTheme.setOnCheckedChangeListener { _: CompoundButton?, isChecked: Boolean ->
            mainViewModel.saveThemeSetting(isChecked)
        }

        dailyReminder.setOnCheckedChangeListener { _: CompoundButton?, isChecked: Boolean ->
            mainViewModel.setDailyReminder(isChecked)
            if (isChecked) {
                fetchLocalEvents()
            }
        }
    }

    private fun fetchLocalEvents() {
        eventDao.getAllEvents().observe(viewLifecycleOwner) { result ->
            if (result != null){
                val filteredData = result.filter { event -> event.isActive == true }
                filteredData.firstOrNull().let {
                    val title = it?.name
                    val message = "Event starts at: ${it?.beginTime}"
                    val url = it?.link
                    sendNotification(title.toString(), message, url.toString())
                }
            }
        }
    }

    private fun sendNotification(title: String, message: String, url: String) {
        val notificationManager = requireContext().getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
        val pendingIntent = PendingIntent.getActivity(
            requireContext(),
            0,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val builder = NotificationCompat.Builder(requireContext(), CHANNEL_ID)
            .setContentTitle(title)
            .setSmallIcon(R.drawable.baseline_notifications_active_24)
            .setContentText(message)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setSubText(getString(R.string.notification_subtext))
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                CHANNEL_NAME,
                NotificationManager.IMPORTANCE_DEFAULT
            ).apply {
                description = "Your notification channel description"
            }
            notificationManager.createNotificationChannel(channel)
        }

        notificationManager.notify(NOTIFICATION_ID, builder.build())
    }

    companion object {
        private const val NOTIFICATION_ID = 1
        private const val CHANNEL_ID = "channel_01"
        private const val CHANNEL_NAME = "dicoding channel"
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}
