package com.esobol.Railway.dataAdapters

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.esobol.Railway.R
import com.esobol.Railway.viewHolders.TicketListViewHolder
import com.esobol.Railway.models.Ticket
import com.esobol.Railway.models.TicketWithPlaces
import java.text.DateFormat
import java.text.SimpleDateFormat

class TicketListDataAdapter(val context: Context, var tickets: ArrayList<TicketWithPlaces>): RecyclerView.Adapter<TicketListViewHolder>() {

    var listener: Listener? = null

    override fun onCreateViewHolder(p0: ViewGroup, index: Int): TicketListViewHolder {
        val view = LayoutInflater.from(p0.context).inflate(R.layout.ticket_list_view_holder, p0, false)
        return TicketListViewHolder(view)
    }

    override fun getItemCount(): Int {
        return tickets.size
    }

    override fun onBindViewHolder(viewHolder: TicketListViewHolder, index: Int) {
        val ticket = tickets[index].ticket
        val formatter = SimpleDateFormat("EEEE, d MMMM, HH:mm")
        viewHolder.stationsTextView.text =
            context.getString(R.string.ticket_list_stations_title, ticket.source, ticket.destination)
        viewHolder.dateTextView.text = formatter.format(ticket.departureDate)
        viewHolder.itemView.setOnClickListener {
            listener?.onTicketClick(tickets[index])
        }
    }

    fun updateTickets(tickets: ArrayList<TicketWithPlaces>) {
        this.tickets = tickets
        notifyDataSetChanged()
    }

    interface Listener {
        fun onTicketClick(ticket: TicketWithPlaces)
    }
}