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
import android.view.View
import android.widget.DatePicker
import android.widget.EditText
import android.widget.TimePicker
import com.esobol.Railway.R
import java.lang.Exception
import java.text.SimpleDateFormat
import java.util.*

class AddTicketActivity : AppCompatActivity(), DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener, View.OnClickListener {

    private lateinit var departureDateEditText: EditText
    private lateinit var departureTimeEditText: EditText
    private lateinit var arrivalDateEditText: EditText
    private lateinit var arrivalTimeEditText: EditText
    private lateinit var departureDate: Calendar
    private lateinit var arrivalDate: Calendar
    private var activeEditTextId: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_ticket)
        departureDate = Calendar.getInstance()
        arrivalDate = Calendar.getInstance()
        arrivalDate.add(Calendar.HOUR, 1)

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
        val color: Int
        if (departureDate > arrivalDate) {
            color = ContextCompat.getColor(this, R.color.errorTextColor)
        } else {
            color = ContextCompat.getColor(this, R.color.defaultTextColor)
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
    }
}
