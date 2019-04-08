package com.esobol.Railway.activities

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import com.esobol.Railway.MyApplication
import com.esobol.Railway.R
import com.esobol.Railway.database.TicketRepository
import com.esobol.Railway.models.TicketWithPlaces
import javax.inject.Inject

class TicketDetailsActivity : AppCompatActivity() {

    @Inject
    lateinit var ticketRepository: TicketRepository
    lateinit var sourceTextView: TextView
    lateinit var destinationTextView: TextView
    lateinit var ticketId: String
    var ticket: TicketWithPlaces? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ticket_details)
        MyApplication.component.inject(this)

        ticketId = intent.getStringExtra(TicketListActivity.TICKET_ID)

        sourceTextView = findViewById(R.id.source_text_view)
        destinationTextView = findViewById(R.id.destination_text_view)

        fetchTicket()
    }

    fun fetchTicket() {
        val task = ticketRepository.getTicket(ticketId)
        task.listener = object : TicketRepository.FetchTicketTask.Listener {
            override fun onDataLoaded(result: TicketWithPlaces) {
                ticket = result
                reloadData()
            }
        }
        task.execute()
    }

    fun reloadData() {
        sourceTextView.text = ticket?.ticket?.source
        destinationTextView.text = ticket?.ticket?.destination
    }
}
