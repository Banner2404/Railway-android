package com.esobol.Railway.dataAdapters

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.esobol.Railway.R
import com.esobol.Railway.viewHolders.TicketListViewHolder
import com.esobol.Railway.models.Ticket

class TicketListDataAdapter(val context: Context): RecyclerView.Adapter<TicketListViewHolder>() {

    private val tickets: ArrayList<Ticket> = arrayListOf(
        Ticket("Минск", "Брест"),
        Ticket("Минск", "Брест"),
        Ticket("TEst", "Station"))

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): TicketListViewHolder {
        val view = LayoutInflater.from(p0.context).inflate(R.layout.ticket_list_view_holder, p0, false)
        return TicketListViewHolder(view)
    }

    override fun getItemCount(): Int {
        return tickets.size
    }

    override fun onBindViewHolder(viewHolder: TicketListViewHolder, index: Int) {
        val ticket = tickets[index]
        viewHolder.textView.text =
            context.getString(R.string.ticket_list_stations_title, ticket.source, ticket.destination)
    }
}