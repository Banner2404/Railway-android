package com.esobol.Railway.dataAdapters

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.esobol.Railway.R
import com.esobol.Railway.viewHolders.AddTicketTextViewHolder

class AddTicketDataAdapter: RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        const val TEXT_ROW = 1
    }

    override fun getItemViewType(position: Int): Int {
        return TEXT_ROW
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.add_ticket_text_view_holder, parent, false)
        return AddTicketTextViewHolder(view)
    }

    override fun getItemCount(): Int {
        return 100
    }

    override fun onBindViewHolder(viewHolder: RecyclerView.ViewHolder, viewType: Int) {
        val viewHolder = viewHolder as AddTicketTextViewHolder
        viewHolder.textView.text = "Hello world"
    }


}