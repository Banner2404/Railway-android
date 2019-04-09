package com.esobol.Railway.activities

import android.app.Activity
import android.content.DialogInterface
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import com.esobol.Railway.MyApplication
import com.esobol.Railway.R
import com.esobol.Railway.database.TicketRepository
import com.esobol.Railway.models.Place
import com.esobol.Railway.models.TicketWithPlaces
import com.esobol.Railway.views.PlaceView
import java.text.SimpleDateFormat
import javax.inject.Inject

class TicketDetailsActivity : AppCompatActivity() {

    @Inject
    lateinit var ticketRepository: TicketRepository
    lateinit var sourceTextView: TextView
    lateinit var destinationTextView: TextView
    lateinit var departureTimeTextView: TextView
    lateinit var arrivalTimeTextView: TextView
    lateinit var departureDateTextView: TextView
    lateinit var placesLinearLayout: LinearLayout
    lateinit var ticketId: String
    var ticket: TicketWithPlaces? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ticket_details)
        MyApplication.component.inject(this)

        ticketId = intent.getStringExtra(TicketListActivity.TICKET_ID)

        sourceTextView = findViewById(R.id.source_text_view)
        destinationTextView = findViewById(R.id.destination_text_view)
        departureTimeTextView = findViewById(R.id.departure_time_text_view)
        arrivalTimeTextView = findViewById(R.id.arrival_time_text_view)
        departureDateTextView = findViewById(R.id.departure_date_text_view)
        placesLinearLayout = findViewById(R.id.places_linear_layout)

        fetchTicket()

        findViewById<Button>(R.id.delete_button).setOnClickListener {
            showDeleteDialogue()
        }

        findViewById<Button>(R.id.edit_button).setOnClickListener {
            editTicket()
        }
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
        val timeFormatter = SimpleDateFormat("HH:mm")
        val dateFormatter = SimpleDateFormat("EEEE, d MMMM")

        departureTimeTextView.text = timeFormatter.format(ticket?.ticket?.departureDate)
        arrivalTimeTextView.text = timeFormatter.format(ticket?.ticket?.arrivalDate)
        departureDateTextView.text = dateFormatter.format(ticket?.ticket?.departureDate)

        placesLinearLayout.removeAllViews()
        ticket?.places?.forEach { addPlaceView(it) }
    }

    fun addPlaceView(place: Place) {
        val view = layoutInflater.inflate(R.layout.place_view, placesLinearLayout, false) as PlaceView
        view.carriageTextView.text = place.carriage.toString()
        view.seatTextView.text = place.seat
        placesLinearLayout.addView(view)
    }

    fun showDeleteDialogue() {
        AlertDialog.Builder(this)
            .setTitle(getString(R.string.delete_ticket_title))
            .setMessage(getString(R.string.delete_ticket_message))
            .setPositiveButton(android.R.string.yes, object: DialogInterface.OnClickListener {
                override fun onClick(dialog: DialogInterface?, which: Int) {
                    deleteTicket()
                }
            })
            .setNegativeButton(android.R.string.no, null)
            .show()
    }

    fun deleteTicket() {
        ticket?.let {
            ticketRepository.delete(it)
        }
        setResult(Activity.RESULT_OK)
        finish()
    }

    fun editTicket() {
        val intent = Intent(this, AddTicketActivity::class.java)
        intent.putExtra(AddTicketActivity.TICKET_ID, ticketId)
        startActivityForResult(intent, 0)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        fetchTicket()
    }
}
