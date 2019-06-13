package com.esobol.Railway.notifications

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Build
import android.preference.PreferenceManager
import android.support.v4.app.NotificationCompat
import android.support.v4.app.NotificationManagerCompat
import com.esobol.Railway.MyApplication
import com.esobol.Railway.R
import com.esobol.Railway.activities.TicketListActivity
import com.esobol.Railway.database.TicketRepository
import com.esobol.Railway.models.NotificationAlert

object NotificationManager {

    val IS_ENABLED_KEY = "IS_ENABLED_KEY"

    var notificationAlerts: ArrayList<NotificationAlert> = arrayListOf()
    val preferences = PreferenceManager.getDefaultSharedPreferences(MyApplication.context)
    var isEnabled: Boolean
        get() = preferences.getBoolean(IS_ENABLED_KEY, false)
        set(value) {
            preferences.edit().putBoolean(IS_ENABLED_KEY, value).apply()
            setupNotifications()
        }

    fun loadNotifications(listener: Listener) {
        val task = TicketRepository.getNotificationAlerts()
        task.listener = object : TicketRepository.FetchNotificationAlertsTask.Listener {
            override fun onDataLoaded(notifications: Set<NotificationAlert>) {
                notificationAlerts = ArrayList(notifications)
                listener.onNotificationsLoaded()
            }
        }
        task.execute()
    }

    fun add(alert: NotificationAlert) {
        notificationAlerts.add(alert)
        TicketRepository.addNotificationAlert(alert)
        setupNotifications()
    }

    fun remove(alert: NotificationAlert) {
        notificationAlerts.remove(alert)
        TicketRepository.deleteNotificationAlert(alert)
        setupNotifications()
    }

    fun availableAlerts() : List<NotificationAlert> {
        return NotificationAlert.values().filter { !notificationAlerts.contains(it) }
    }

    private fun setupNotifications() {
        //alarmScheduler.scheduleAlarms()
    }

    interface Listener {
        fun onNotificationsLoaded()
    }
}