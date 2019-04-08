package com.esobol.Railway.views

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.EditText
import android.widget.LinearLayout
import com.esobol.Railway.R
import kotlinx.android.synthetic.main.edit_place_view.view.*

class EditPlaceView(context: Context, attrs: AttributeSet): LinearLayout(context, attrs) {

    var placeId: String? = null
    val removeButton: View
        get() = findViewById(R.id.remove_button)

    val carriageEditText: EditText
        get() = findViewById(R.id.carriage_edit_text)

    val seatEditText: EditText
        get() = findViewById(R.id.seat_edit_text)


}