package com.esobol.Railway.database

import android.content.Context
import com.esobol.Railway.models.NotificationAlert
import javax.inject.Inject
import javax.inject.Singleton

class NotificationManager @Inject constructor(var ticketRepository: TicketRepository) {

    var notificationAlerts: ArrayList<NotificationAlert> = arrayListOf()

    fun loadNotifications(listener: Listener) {
        val task = ticketRepository.getNotificationAlerts()
        task.listener = object : TicketRepository.FetchNotificationAlertsTask.Listener {
            override fun onDataLoaded(tickets: Set<NotificationAlert>) {
                notificationAlerts = ArrayList(tickets)
                listener.onNotificationsLoaded()
            }
        }
        task.execute()
    }

    fun add(alert: NotificationAlert) {
        notificationAlerts.add(alert)
        ticketRepository.addNotificationAlert(alert)
    }

    fun remove(alert: NotificationAlert) {
        notificationAlerts.remove(alert)
        ticketRepository.deleteNotificationAlert(alert)
    }

    fun availableAlerts() : List<NotificationAlert> {
        return NotificationAlert.values().filter { !notificationAlerts.contains(it) }
    }

    interface Listener {
        fun onNotificationsLoaded()
    }
}