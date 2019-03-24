package com.esobol.Railway.dataAdapters

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.esobol.Railway.R
import com.esobol.Railway.viewHolders.TicketListViewHolder
import com.esobol.Railway.models.Ticket
import java.text.DateFormat
import java.text.SimpleDateFormat

class TicketListDataAdapter(val context: Context, var tickets: ArrayList<Ticket>): RecyclerView.Adapter<TicketListViewHolder>() {

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): TicketListViewHolder {
        val view = LayoutInflater.from(p0.context).inflate(R.layout.ticket_list_view_holder, p0, false)
        return TicketListViewHolder(view)
    }

    override fun getItemCount(): Int {
        return tickets.size
    }

    override fun onBindViewHolder(viewHolder: TicketListViewHolder, index: Int) {
        val ticket = tickets[index]
        val formatter = SimpleDateFormat("EEEE, d MMMM, HH:mm")
        viewHolder.stationsTextView.text =
            context.getString(R.string.ticket_list_stations_title, ticket.source, ticket.destination)
        viewHolder.dateTextView.text = formatter.format(ticket.departureDate)
    }

    fun updateTickets(tickets: ArrayList<Ticket>) {
        this.tickets = tickets
        notifyDataSetChanged()
    }
}