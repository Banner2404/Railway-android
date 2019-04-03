package com.esobol.Railway.activities

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.graphics.Color
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v7.widget.AppCompatEditText
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.text.Editable
import android.text.TextWatcher
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.DatePicker
import android.widget.EditText
import android.widget.TimePicker
import com.esobol.Railway.MyApplication
import com.esobol.Railway.R
import com.esobol.Railway.database.TicketRepository
import com.esobol.Railway.models.Ticket
import java.lang.Exception
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

class AddTicketActivity : AppCompatActivity(), DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener, View.OnClickListener, TextWatcher {

    @Inject
    lateinit var ticketRepository: TicketRepository
    private lateinit var sourceEditText: EditText
    private lateinit var destinationEditText: EditText
    private lateinit var departureDateEditText: EditText
    private lateinit var departureTimeEditText: EditText
    private lateinit var arrivalDateEditText: EditText
    private lateinit var arrivalTimeEditText: EditText
    private lateinit var departureDate: Calendar
    private lateinit var arrivalDate: Calendar
    private var activeEditTextId: Int = 0
    private var isValid = false

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

        departureDateEditText.setOnClickListener(this)
        departureTimeEditText.setOnClickListener(this)
        arrivalDateEditText.setOnClickListener(this)
        arrivalTimeEditText.setOnClickListener(this)

        updateDepartureDate()
        updateArrivalDate()

        sourceEditText.addTextChangedListener(this)
        destinationEditText.addTextChangedListener(this)
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
            ticketRepository.create(ticket)
            return true
        } else {
            return false
        }
    }

    fun validateInput() {
        val isValidSource = !sourceEditText.text.toString().isBlank()
        val isValidDestination = !destinationEditText.text.toString().isBlank()
        val isValidDates = departureDate < arrivalDate
        isValid = isValidSource && isValidDestination && isValidDates
        updateArrivalColor(isValidDates)
        invalidateOptionsMenu()
    }
}
