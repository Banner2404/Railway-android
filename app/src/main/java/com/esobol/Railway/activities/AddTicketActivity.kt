package com.esobol.Railway.activities

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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_ticket)
        MyApplication.component.inject(this)
        departureDate = Calendar.getInstance()
        arrivalDate = Calendar.getInstance()
        arrivalDate.add(Calendar.HOUR, 1)

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

        updateDepartureDate()
        updateArrivalDate()

        sourceEditText.addTextChangedListener(this)
        destinationEditText.addTextChangedListener(this)
        addPlaceView()

        addPlaceButton.setOnClickListener {
            addPlaceView()
        }
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
            val ticket = Ticket(sourceEditText.text.toString(), destinationEditText.text.toString(), departureDate.time, arrivalDate.time)
            val ticketWithPlaces = TicketWithPlaces()
            ticketWithPlaces.ticket = ticket
            places.forEach { it.ticketId = ticket.id }
            ticketWithPlaces.places = places
            ticketRepository.create(ticketWithPlaces)
            return true
        } else {
            return false
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
        val view = layoutInflater.inflate(R.layout.edit_place_view, linearLayout, false) as EditPlaceView
        val index = linearLayout.indexOfChild(addPlaceButton.parent as View)
        linearLayout.addView(view, index)
        val newPlace = Place()
        places.add(newPlace)
        view.placeId = newPlace.id
        view.removeButton.setOnClickListener {
            removePlaceView(it)
        }

        view.carriageEditText.addTextChangedListener(object : TextWatcher {
            override fun onTextChanged(string: CharSequence?, start: Int, before: Int, count: Int) {
                newPlace.carriage = string.toString().toIntOrNull() ?: 0
                validateInput()
            }

            override fun afterTextChanged(s: Editable?) {

            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }
        })

        view.seatEditText.addTextChangedListener(object : TextWatcher {
            override fun onTextChanged(string: CharSequence?, start: Int, before: Int, count: Int) {
                newPlace.seat = string.toString()
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
