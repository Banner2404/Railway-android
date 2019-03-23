package com.esobol.Railway.activities

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import com.esobol.Railway.R
import com.esobol.Railway.dataAdapters.TicketListDataAdapter

class TicketListActivity : AppCompatActivity() {

    lateinit var recyclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ticket_list)

        recyclerView = findViewById(R.id.tickets_recycler_view)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = TicketListDataAdapter(this)
    }
}
