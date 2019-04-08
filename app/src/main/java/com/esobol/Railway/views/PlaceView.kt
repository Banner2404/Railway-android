package com.esobol.Railway.views

import android.content.Context
import android.support.constraint.ConstraintLayout
import android.util.AttributeSet
import android.view.View
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import com.esobol.Railway.R

class PlaceView(context: Context, attrs: AttributeSet): ConstraintLayout(context, attrs) {

    val carriageTextView: TextView
        get() = findViewById(R.id.carriage_text_view)

    val seatTextView: TextView
        get() = findViewById(R.id.seat_text_view)


}