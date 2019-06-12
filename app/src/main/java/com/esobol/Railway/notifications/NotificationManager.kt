package com.esobol.Railway.notifications

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.preference.PreferenceManager
import android.support.v4.app.NotificationCompat
import android.support.v4.app.NotificationManagerCompat
import com.esobol.Railway.R
import com.esobol.Railway.activities.TicketListActivity
import com.esobol.Railway.database.TicketRepository
import com.esobol.Railway.models.NotificationAlert
import javax.inject.Inject

class NotificationManager @Inject constructor(var ticketRepository: TicketRepository, var context: Context,
                                              var alarmScheduler: AlarmScheduler) {

    companion object {
        val IS_ENABLED_KEY = "IS_ENABLED_KEY"
    }

    var notificationAlerts: ArrayList<NotificationAlert> = arrayListOf()
    var isEnabled: Boolean
        get() = PreferenceManager.getDefaultSharedPreferences(context).getBoolean(IS_ENABLED_KEY, false)
        set(value) {
            PreferenceManager.getDefaultSharedPreferences(context).edit().putBoolean(IS_ENABLED_KEY, value).apply()
        }

    fun loadNotifications(listener: Listener) {
        val task = ticketRepository.getNotificationAlerts()
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
        ticketRepository.addNotificationAlert(alert)
        setupNotifications()
    }

    fun remove(alert: NotificationAlert) {
        notificationAlerts.remove(alert)
        ticketRepository.deleteNotificationAlert(alert)
    }

    fun availableAlerts() : List<NotificationAlert> {
        return NotificationAlert.values().filter { !notificationAlerts.contains(it) }
    }

    private fun setupNotifications() {
        alarmScheduler.scheduleAlarms()
    }

    interface Listener {
        fun onNotificationsLoaded()
    }
}