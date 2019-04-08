package com.esobol.Railway.activities

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.Menu
import android.view.MenuItem
import com.esobol.Railway.DaggerInjectComponent
import com.esobol.Railway.MyApplication
import com.esobol.Railway.R
import com.esobol.Railway.dataAdapters.TicketListDataAdapter
import com.esobol.Railway.database.TicketRepository
import com.esobol.Railway.models.Ticket
import com.esobol.Railway.models.TicketWithPlaces
import javax.inject.Inject

class TicketListActivity : AppCompatActivity(), TicketListDataAdapter.Listener {

    @Inject
    lateinit var repository: TicketRepository
    lateinit var recyclerView: RecyclerView
    lateinit var adapter: TicketListDataAdapter

    companion object {
        val TICKET_ID = "TICKET_ID"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ticket_list)
        MyApplication.component.inject(this)

        recyclerView = findViewById(R.id.tickets_recycler_view)
        recyclerView.layoutManager = LinearLayoutManager(this)
        adapter = TicketListDataAdapter(this, arrayListOf())
        adapter.listener = this
        recyclerView.adapter = adapter

        reloadData()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.add_ticket) {
            val intent = Intent(this, AddTicketActivity::class.java)
            startActivityForResult(intent, 0)
            return true
        } else {
            return super.onOptionsItemSelected(item)
        }
    }

    override fun onTicketClick(ticket: TicketWithPlaces) {
        val intent = Intent(this, TicketDetailsActivity::class.java)
        intent.putExtra(TICKET_ID, ticket.ticket.id)
        startActivity(intent)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        reloadData()
    }

    fun reloadData() {
        val fetchTask = repository.getTickets()
        fetchTask.listener = object : TicketRepository.FetchTask.Listener {
            override fun onDataLoaded(tickets: ArrayList<TicketWithPlaces>) {
                adapter.updateTickets(tickets)
            }
        }

        fetchTask.execute()
    }
}
