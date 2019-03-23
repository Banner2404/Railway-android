package com.esobol.Railway.viewHolders

import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.TextView
import com.esobol.Railway.R
import kotlinx.android.synthetic.main.ticket_list_view_holder.view.*

class TicketListViewHolder(view: View): RecyclerView.ViewHolder(view) {

    val stationsTextView: TextView = view.findViewById(R.id.stations_text_view)
    val dateTextView: TextView = view.findViewById(R.id.date_text_view)
}