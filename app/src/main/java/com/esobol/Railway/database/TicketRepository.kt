package com.esobol.Railway.database

import android.arch.persistence.room.Room
import android.content.Context
import android.os.AsyncTask
import com.esobol.Railway.activities.TicketListActivity
import com.esobol.Railway.models.*
import dagger.Component
import javax.inject.Inject
import javax.inject.Named

class TicketRepository @Inject constructor(var context: Context) {

    val database: Database = Room.databaseBuilder(context, Database::class.java, "database")
        .fallbackToDestructiveMigration()
        .build()

    fun getTickets(): FetchTask {
        return FetchTask(database)
    }

    fun getTicket(id: String): FetchTicketTask {
        return FetchTicketTask(database, id)
    }

    fun create(ticket: TicketWithPlaces) {
        CreateTicketTask(database, ticket.ticket).execute()
        for (place in ticket.places) {
            CreatePlaceTask(database, place).execute()
        }
    }

    fun delete(ticket: TicketWithPlaces) {
        ticket.places.forEach {
            DeletePlaceTask(database, it).execute()
        }

        DeleteTicketTask(database, ticket.ticket).execute()
    }

    fun update(ticket: TicketWithPlaces) {
        UpdateTicketTask(database, ticket.ticket).execute()
        for (place in ticket.places) {
            CreatePlaceTask(database, place).execute()
        }
    }

    fun getNotificationAlerts() : FetchNotificationAlertsTask {
        return FetchNotificationAlertsTask(database)
    }

    fun addNotificationAlert(alert: NotificationAlert) {
        CreateNotificationAlertTask(database, alert).execute()
    }

    class FetchTask(val database: Database): AsyncTask<Void, Void, ArrayList<TicketWithPlaces>>() {

        var listener: Listener? = null

        override fun doInBackground(vararg args: Void?): ArrayList<TicketWithPlaces> {
            return ArrayList(database.ticketDao().getAllTickets())
        }

        override fun onPostExecute(result: ArrayList<TicketWithPlaces>?) {
            super.onPostExecute(result)
            result?.let {
                listener?.onDataLoaded(it)
            }
        }

        interface Listener {
            fun onDataLoaded(tickets: ArrayList<TicketWithPlaces>)
        }
    }

    class FetchTicketTask(val database: Database, val id: String): AsyncTask<Void, Void, TicketWithPlaces>() {

        var listener: Listener? = null

        override fun doInBackground(vararg args: Void?): TicketWithPlaces {
            return database.ticketDao().getTicketWithId(id)
        }

        override fun onPostExecute(result: TicketWithPlaces?) {
            super.onPostExecute(result)
            result?.let {
                listener?.onDataLoaded(it)
            }
        }

        interface Listener {
            fun onDataLoaded(tickets: TicketWithPlaces)
        }
    }

    class CreateTicketTask(val database: Database, val ticket: Ticket): AsyncTask<Void, Void, Void>() {

        override fun doInBackground(vararg params: Void?): Void? {
            database.ticketDao().insertTicket(ticket)
            return null
        }
    }

    class UpdateTicketTask(val database: Database, val ticket: Ticket): AsyncTask<Void, Void, Void>() {

        override fun doInBackground(vararg params: Void?): Void? {
            database.ticketDao().updateTicket(ticket)
            return null
        }
    }

    class CreatePlaceTask(val database: Database, val place: Place): AsyncTask<Void, Void, Void>() {

        override fun doInBackground(vararg params: Void?): Void? {
            database.placeDao().insertPlace(place)
            return null
        }
    }

    class DeletePlaceTask(val database: Database, val place: Place): AsyncTask<Void, Void, Void>() {

        override fun doInBackground(vararg params: Void?): Void? {
            database.placeDao().deletePlace(place)
            return null
        }
    }

    class DeleteTicketTask(val database: Database, val ticket: Ticket): AsyncTask<Void, Void, Void>() {

        override fun doInBackground(vararg params: Void?): Void? {
            database.ticketDao().deleteTicket(ticket)
            return null
        }
    }

    class FetchNotificationAlertsTask(val database: Database): AsyncTask<Void, Void, Set<NotificationAlert>>() {

        var listener: Listener? = null

        override fun doInBackground(vararg args: Void?): Set<NotificationAlert> {
            return database.notificationAlertDao().getNotificationAlerts().map { NotificationAlert.values()[it.value] }.toSet()
        }

        override fun onPostExecute(result: Set<NotificationAlert>?) {
            super.onPostExecute(result)
            result?.let {
                listener?.onDataLoaded(it)
            }
        }

        interface Listener {
            fun onDataLoaded(tickets: Set<NotificationAlert>)
        }
    }

    class CreateNotificationAlertTask(val database: Database, val alert: NotificationAlert): AsyncTask<Void, Void, Void>() {

        override fun doInBackground(vararg params: Void?): Void? {
            val entity = NotificationAlertEntity()
            entity.value = alert.ordinal
            database.notificationAlertDao().insertNotificationAlert(entity)
            return null
        }
    }
}