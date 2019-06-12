package com.esobol.Railway.notifications

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.support.v4.app.NotificationCompat
import android.support.v4.app.NotificationManagerCompat
import com.esobol.Railway.R
import com.esobol.Railway.activities.TicketListActivity

class NotificationSender(val context: Context) {

    companion object {
        val CHANNEL_ID = "NOTIFICATION_CHANNEL_ID"
    }

    val notificationName = "trainNotification"
    val notificationChannelId = "${context.packageName}-$notificationName"

    fun sendNotification() {
        createNotificationChannel()

        val notificationBuilder = NotificationCompat.Builder(context, notificationChannelId).apply {
            setSmallIcon(R.drawable.delete_button)
            setContentTitle("Title")
            setContentText("Text")
            setStyle(NotificationCompat.BigTextStyle().bigText("Big text"))
            priority = NotificationCompat.PRIORITY_DEFAULT
            setAutoCancel(true)

            val intent = Intent(context, TicketListActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK

            val pendingIntent = PendingIntent.getActivity(context, 0, intent, 0)
            setContentIntent(pendingIntent)
        }

        val notificationManager = NotificationManagerCompat.from(context)
        notificationManager.notify(1001, notificationBuilder.build())
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            val channel = NotificationChannel(notificationChannelId, notificationName, NotificationManager.IMPORTANCE_DEFAULT)
            channel.description = "Train notifications."
            channel.setShowBadge(false)

            val notificationManager = context.getSystemService(NotificationManager::class.java)
            notificationManager.createNotificationChannel(channel)
        }
    }
}