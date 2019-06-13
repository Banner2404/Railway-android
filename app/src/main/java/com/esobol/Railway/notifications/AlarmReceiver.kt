package com.esobol.Railway.notifications

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.esobol.Railway.MyApplication
import com.esobol.Railway.database.TicketRepository
import com.esobol.Railway.models.TicketWithPlaces

class AlarmReceiver: BroadcastReceiver() {

    val ticketRepository = TicketRepository

    override fun onReceive(context: Context?, intent: Intent?) {
        if (context != null && intent != null) {
            val ticketId = intent.getStringExtra(AlarmScheduler.TICKET_ID)
            val task = ticketRepository.getTicket(ticketId)
            task.listener = object : TicketRepository.FetchTicketTask.Listener {
                override fun onDataLoaded(ticket: TicketWithPlaces) {
                    val sender = NotificationSender(context)
                    sender.sendNotification(ticket)
                }
            }
            task.execute()
        }

    }
}