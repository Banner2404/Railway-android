package com.esobol.Railway.database

import android.arch.persistence.room.Room
import android.content.Context
import android.os.AsyncTask
import com.esobol.Railway.activities.TicketListActivity
import com.esobol.Railway.models.Place
import com.esobol.Railway.models.Ticket
import com.esobol.Railway.models.TicketWithPlaces
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

    fun create(ticket: TicketWithPlaces) {
        CreateTicketTask(database, ticket.ticket).execute()
        for (place in ticket.places) {
            CreatePlaceTask(database, place).execute()
        }
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

    class CreateTicketTask(val database: Database, val ticket: Ticket): AsyncTask<Void, Void, Void>() {

        override fun doInBackground(vararg params: Void?): Void? {
            database.ticketDao().insertTicket(ticket)
            return null
        }
    }

    class CreatePlaceTask(val database: Database, val place: Place): AsyncTask<Void, Void, Void>() {

        override fun doInBackground(vararg params: Void?): Void? {
            database.placeDao().insertPlace(place)
            return null
        }
    }
}