package com.esobol.Railway.activities

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.Menu
import android.view.MenuItem
import com.esobol.Railway.R
import com.esobol.Railway.dataAdapters.TicketListDataAdapter
import com.esobol.Railway.database.TicketRepository
import com.esobol.Railway.models.Ticket

class TicketListActivity : AppCompatActivity() {

    lateinit var repository: TicketRepository
    lateinit var recyclerView: RecyclerView
    lateinit var adapter: TicketListDataAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ticket_list)

        repository = TicketRepository(this)
        //repository.createNew()

        recyclerView = findViewById(R.id.tickets_recycler_view)
        recyclerView.layoutManager = LinearLayoutManager(this)
        adapter = TicketListDataAdapter(this, arrayListOf())
        recyclerView.adapter = adapter

        val fetchTask = repository.getTickets()
        fetchTask.listener = object : TicketRepository.FetchTask.Listener {
            override fun onDataLoaded(tickets: ArrayList<Ticket>) {
                adapter.updateTickets(tickets)
            }
        }

        fetchTask.execute()

    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.add_ticket) {
            val intent = Intent(this, AddTicketActivity::class.java)
            startActivity(intent)
            return true
        } else {
            return super.onOptionsItemSelected(item)
        }
    }
}
