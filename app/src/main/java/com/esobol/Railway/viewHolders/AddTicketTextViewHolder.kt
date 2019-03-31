package com.esobol.Railway.viewHolders

import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.TextView
import com.esobol.Railway.R
import kotlinx.android.synthetic.main.add_ticket_text_view_holder.view.*

class AddTicketTextViewHolder(val view: View): RecyclerView.ViewHolder(view) {

    val textView: TextView = view.findViewById(R.id.text_view)
}