package com.esobol.Railway.database

import android.arch.persistence.room.Room
import android.content.Context
import android.os.AsyncTask
import com.esobol.Railway.models.Ticket

class TicketRepository(val context: Context) {

    val database: Database = Room.databaseBuilder(context, Database::class.java, "database").build()

    fun getTickets(): FetchTask {
        return FetchTask(database)
    }

    fun createNew() {
        val ticket = Ticket("from", "to")
        CreateTask(database, ticket).execute()
    }

    class FetchTask(val database: Database): AsyncTask<Void, Void, ArrayList<Ticket>>() {

        var listener: Listener? = null

        override fun doInBackground(vararg args: Void?): ArrayList<Ticket> {
            return ArrayList(database.ticketDao().getAllTickets())
        }

        override fun onPostExecute(result: ArrayList<Ticket>?) {
            super.onPostExecute(result)
            result?.let {
                listener?.onDataLoaded(it)
            }
        }

        interface Listener {
            fun onDataLoaded(tickets: ArrayList<Ticket>)
        }
    }

    class CreateTask(val database: Database, val ticket: Ticket): AsyncTask<Void, Void, Void>() {

        override fun doInBackground(vararg params: Void?): Void? {
            database.ticketDao().insertTicket(ticket)
            return null
        }
    }

}