package com.esobol.Railway.viewHolders

import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.TextView
import com.esobol.Railway.R
import kotlinx.android.synthetic.main.ticket_list_view_holder.view.*

class TicketListViewHolder(view: View): RecyclerView.ViewHolder(view) {

    val textView: TextView = view.findViewById(R.id.textView)
}