package com.esobol.Railway.notifications

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.preference.PreferenceManager
import com.esobol.Railway.activities.SettingsActivity
import com.esobol.Railway.database.TicketRepository
import com.esobol.Railway.models.NotificationAlert
import com.esobol.Railway.models.Ticket
import com.esobol.Railway.models.TicketWithPlaces
import org.json.JSONArray
import java.util.*
import javax.inject.Inject
import kotlin.collections.ArrayList

class AlarmScheduler @Inject constructor(var context: Context, var ticketRepository: TicketRepository) {

    val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

    companion object {
        val SCHEDULED_ALARMS = "SCHEDULED_ALARMS"
    }

    fun scheduleAlarms() {
        cancelAlarms()
        val task = ticketRepository.getTickets()
        task.listener = object : TicketRepository.FetchTask.Listener {

            override fun onDataLoaded(tickets: ArrayList<TicketWithPlaces>) {
                scheduleAlarms(tickets)
            }
        }
        task.execute()
    }

    private fun cancelAlarms() {
        val preferences = PreferenceManager.getDefaultSharedPreferences(context)
        val jsonArray = JSONArray(preferences.getString(SCHEDULED_ALARMS, "[]"))
        for (i in 0..(jsonArray.length() - 1)) {
            val identifier = jsonArray.getInt(i)
            restorePendingIntent(identifier).let {
                alarmManager.cancel(it)
            }

        }
    }

    private fun scheduleAlarms(tickets: ArrayList<TicketWithPlaces>) {
        val task = ticketRepository.getNotificationAlerts()
        task.listener = object : TicketRepository.FetchNotificationAlertsTask.Listener {
            override fun onDataLoaded(notifications: Set<NotificationAlert>) {
                scheduleAlarms(tickets, notifications)
            }
        }
        task.execute()
    }

    private fun scheduleAlarms(tickets: ArrayList<TicketWithPlaces>, notifications: Set<NotificationAlert>) {
        val preferences = PreferenceManager.getDefaultSharedPreferences(context).edit()
        val jsonArray = JSONArray()
        for (ticket in tickets) {
            for (notification in notifications) {
                val intentIdentifier = intentIdentifier(ticket, notification)
                scheduleAlarm(ticket, notification, intentIdentifier)
                jsonArray.put(intentIdentifier)
            }
        }
        preferences.putString(SCHEDULED_ALARMS, jsonArray.toString())
        preferences.apply()
    }

    private fun scheduleAlarm(ticket: TicketWithPlaces, notification: NotificationAlert, identifier: Int) {
        val time = ticket.ticket.departureDate.time - timeForNotification(notification)
        val intent = createPendingIntent(identifier)
        alarmManager.set(AlarmManager.RTC_WAKEUP, time, intent)
    }

    private fun intentIdentifier(ticket: TicketWithPlaces, notification: NotificationAlert): Int {
        return (ticket.ticket.id + notification.toString()).hashCode()
    }

    private fun createPendingIntent(identifier: Int): PendingIntent? {
        val intent = Intent(context, AlarmReceiver::class.java)
        return PendingIntent.getBroadcast(context, identifier, intent, PendingIntent.FLAG_UPDATE_CURRENT)
    }

    private fun restorePendingIntent(identifier: Int): PendingIntent? {
        val intent = Intent(context, AlarmReceiver::class.java)
        return PendingIntent.getBroadcast(context, identifier, intent, PendingIntent.FLAG_NO_CREATE)
    }

    private fun timeForNotification(notification: NotificationAlert) : Long {
        when (notification) {
            NotificationAlert.AT_EVENT_TIME -> return 0
            NotificationAlert.FIVE_MINUTES -> return 5 * 60 * 1000
            NotificationAlert.TEN_MINUTES -> return 10 * 60 * 1000
            NotificationAlert.FIFTEEN_MINUTES -> return 15 * 60 * 1000
            NotificationAlert.HALF_HOUR -> return 30 * 60 * 1000
            NotificationAlert.ONE_HOUR -> return 60 * 60 * 1000
            NotificationAlert.TWO_HOURS -> return 2 * 60 * 60 * 1000
        }
    }
}