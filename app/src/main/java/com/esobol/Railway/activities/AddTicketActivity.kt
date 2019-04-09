package com.esobol.Railway.activities

import android.app.Activity
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.text.Editable
import android.text.TextWatcher
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.DatePicker
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TimePicker
import com.esobol.Railway.MyApplication
import com.esobol.Railway.R
import com.esobol.Railway.database.TicketRepository
import com.esobol.Railway.models.Place
import com.esobol.Railway.models.Ticket
import com.esobol.Railway.models.TicketWithPlaces
import com.esobol.Railway.views.EditPlaceView
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject
import kotlin.collections.ArrayList

class AddTicketActivity : AppCompatActivity(), DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener, View.OnClickListener, TextWatcher {

    @Inject
    lateinit var ticketRepository: TicketRepository
    private lateinit var sourceEditText: EditText
    private lateinit var destinationEditText: EditText
    private lateinit var departureDateEditText: EditText
    private lateinit var departureTimeEditText: EditText
    private lateinit var arrivalDateEditText: EditText
    private lateinit var arrivalTimeEditText: EditText
    private lateinit var linearLayout: LinearLayout
    private lateinit var addPlaceButton: View
    private lateinit var departureDate: Calendar
    private lateinit var arrivalDate: Calendar
    private var activeEditTextId: Int = 0
    private var isValid = false
    private var places: ArrayList<Place> = arrayListOf()
    private lateinit var type: Type
    private var updatedTicket: TicketWithPlaces? = null

    enum class Type {
        CREATE, UPDATE
    }

    companion object {
        val TICKET_ID = "TICKET_ID"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_ticket)
        MyApplication.component.inject(this)

        sourceEditText = findViewById(R.id.source_text_view)
        destinationEditText = findViewById(R.id.destination_text_view)

        departureDateEditText = findViewById(R.id.departure_date_text_view)
        departureTimeEditText = findViewById(R.id.departure_time_text_view)
        arrivalDateEditText = findViewById(R.id.arrival_date_text_view)
        arrivalTimeEditText = findViewById(R.id.arrival_time_text_view)

        linearLayout = findViewById(R.id.linear_layout)
        addPlaceButton = findViewById(R.id.add_place_view)

        departureDateEditText.setOnClickListener(this)
        departureTimeEditText.setOnClickListener(this)
        arrivalDateEditText.setOnClickListener(this)
        arrivalTimeEditText.setOnClickListener(this)

        sourceEditText.addTextChangedListener(this)
        destinationEditText.addTextChangedListener(this)

        addPlaceButton.setOnClickListener {
            addPlaceView()
        }

        departureDate = Calendar.getInstance()
        arrivalDate = Calendar.getInstance()
        arrivalDate.add(Calendar.HOUR, 1)

        if (intent.hasExtra(TICKET_ID)) {
            val ticketId = intent.getStringExtra(TICKET_ID)
            setupForUpdate(ticketId)
        } else {
            setupForCreate()
        }

        updateDepartureDate()
        updateArrivalDate()
    }

    fun setupForCreate() {
        type = Type.CREATE
        addPlaceView()
    }

    fun setupForUpdate(id: String) {
        type = Type.UPDATE
        val task = ticketRepository.getTicket(id)
        task.listener = object : TicketRepository.FetchTicketTask.Listener {
            override fun onDataLoaded(tickets: TicketWithPlaces) {
                updatedTicket = tickets
                setupInitialInfo(tickets)
            }
        }
        task.execute()
    }

    fun setupInitialInfo(ticket: TicketWithPlaces) {
        sourceEditText.setText(ticket.ticket.source)
        destinationEditText.setText(ticket.ticket.destination)
        departureDate.time = ticket.ticket.departureDate
        arrivalDate.time = ticket.ticket.arrivalDate

        ticket.places.forEach {
            addPlaceView(it)
        }
        updateDepartureDate()
        updateArrivalDate()
        validateInput()
    }

    override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
        validateInput()
    }

    override fun afterTextChanged(s: Editable?) {

    }

    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.departure_date_text_view -> showDateDialogue(departureDate, R.id.departure_date_text_view)
            R.id.arrival_date_text_view -> showDateDialogue(arrivalDate, R.id.arrival_date_text_view)
            R.id.departure_time_text_view -> showTimeDialogue(departureDate, R.id.departure_time_text_view)
            R.id.arrival_time_text_view -> showTimeDialogue(arrivalDate, R.id.arrival_time_text_view)
        }
    }

    fun showDateDialogue(date: Calendar, id: Int) {
        activeEditTextId = id
        DatePickerDialog(this, this,
            date.get(Calendar.YEAR),
            date.get(Calendar.MONTH),
            date.get(Calendar.DAY_OF_MONTH)).show()
    }

    fun showTimeDialogue(date: Calendar, id: Int) {
        activeEditTextId = id
        TimePickerDialog(this, this,
            date.get(Calendar.HOUR_OF_DAY),
            date.get(Calendar.MINUTE), true).show()
    }

    fun updateDepartureDate() {
        updateDisplayedDate(departureDateEditText, departureTimeEditText, departureDate)
    }

    fun updateArrivalDate() {
        updateDisplayedDate(arrivalDateEditText, arrivalTimeEditText, arrivalDate)
    }

    fun updateArrivalColor(isValidDates: Boolean) {
        val color: Int
        if (isValidDates) {
            color = ContextCompat.getColor(this, R.color.defaultTextColor)
        } else {
            color = ContextCompat.getColor(this, R.color.errorTextColor)
        }
        arrivalDateEditText.setTextColor(color)
        arrivalTimeEditText.setTextColor(color)
    }

    fun updateDisplayedDate(date: EditText, time: EditText, calendar: Calendar) {
        val dateFormatter = SimpleDateFormat("d MMMM")
        val timeFormatter = SimpleDateFormat("H:mm")
        date.setText(dateFormatter.format(calendar.time))
        time.setText(timeFormatter.format(calendar.time))
    }

    override fun onDateSet(view: DatePicker, year: Int, month: Int, dayOfMonth: Int) {
        when (activeEditTextId) {
            R.id.departure_date_text_view -> {
                departureDate.set(year, month, dayOfMonth)
                updateDepartureDate()
            }
            R.id.arrival_date_text_view -> {
                arrivalDate.set(year, month, dayOfMonth)
                updateArrivalDate()
            }
        }
        validateInput()
    }

    override fun onTimeSet(view: TimePicker, hourOfDay: Int, minute: Int) {
        when (activeEditTextId) {
            R.id.departure_time_text_view -> {
                departureDate.set(Calendar.HOUR_OF_DAY, hourOfDay)
                departureDate.set(Calendar.MINUTE, minute)
                updateDepartureDate()
            }
            R.id.arrival_time_text_view -> {
                arrivalDate.set(Calendar.HOUR_OF_DAY, hourOfDay)
                arrivalDate.set(Calendar.MINUTE, minute)
                updateArrivalDate()
            }
        }
        validateInput()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.add_ticket_menu, menu)
        return true
    }

    override fun onPrepareOptionsMenu(menu: Menu): Boolean {
        menu.findItem(R.id.save_button).setEnabled(isValid)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.save_button) {
            when (type) {
                Type.CREATE -> createTicket()
                Type.UPDATE -> updateTicket()
            }
            setResult(Activity.RESULT_OK)
            finish()
            return true
        } else {
            return false
        }
    }

    fun createTicket() {
        val ticket = Ticket(sourceEditText.text.toString(), destinationEditText.text.toString(), departureDate.time, arrivalDate.time)
        val ticketWithPlaces = TicketWithPlaces()
        ticketWithPlaces.ticket = ticket
        places.forEach { it.ticketId = ticket.id }
        ticketWithPlaces.places = places
        ticketRepository.create(ticketWithPlaces)
    }

    fun updateTicket() {
        updatedTicket?.let {
            val newTicket = Ticket(sourceEditText.text.toString(), destinationEditText.text.toString(), departureDate.time, arrivalDate.time, id = it.ticket.id)
            it.ticket = newTicket
            it.places = places
            ticketRepository.update(it)
        }
    }

    fun validateInput() {
        val isValidSource = !sourceEditText.text.toString().isBlank()
        val isValidDestination = !destinationEditText.text.toString().isBlank()
        val isValidDates = departureDate < arrivalDate
        val placesNotEmpty = !places.isEmpty()
        val isValidPlaces = places.all { it.carriage > 0 && !it.seat.isBlank() }
        isValid = isValidSource && isValidDestination && isValidDates && placesNotEmpty && isValidPlaces
        updateArrivalColor(isValidDates)
        invalidateOptionsMenu()
    }

    fun addPlaceView() {
        val newPlace = Place()
        addPlaceView(newPlace)
    }

    fun addPlaceView(place: Place) {
        val view = layoutInflater.inflate(R.layout.edit_place_view, linearLayout, false) as EditPlaceView
        val index = linearLayout.indexOfChild(addPlaceButton.parent as View)
        linearLayout.addView(view, index)
        places.add(place)
        view.placeId = place.id
        view.removeButton.setOnClickListener {
            removePlaceView(it)
        }

        if (place.carriage > 0) {
            view.carriageEditText.setText(place.carriage.toString())
        }
        view.carriageEditText.addTextChangedListener(object : TextWatcher {
            override fun onTextChanged(string: CharSequence?, start: Int, before: Int, count: Int) {
                place.carriage = string.toString().toIntOrNull() ?: 0
                validateInput()
            }

            override fun afterTextChanged(s: Editable?) {

            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }
        })

        view.seatEditText.setText(place.seat)
        view.seatEditText.addTextChangedListener(object : TextWatcher {
            override fun onTextChanged(string: CharSequence?, start: Int, before: Int, count: Int) {
                place.seat = string.toString()
                validateInput()
            }

            override fun afterTextChanged(s: Editable?) {

            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }
        })
        validateInput()
    }

    fun removePlaceView(view: View) {
        val placeView = view.parent as EditPlaceView
        val index = places.indexOfFirst { it.id == placeView.placeId }
        places.removeAt(index)
        linearLayout.removeView(placeView)
        validateInput()
    }
}
