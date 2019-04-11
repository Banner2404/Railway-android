package com.esobol.Railway.views

import android.content.Context
import android.support.constraint.ConstraintLayout
import android.util.AttributeSet
import android.widget.TextView
import com.esobol.Railway.R

class NotificationView(context: Context, attrs: AttributeSet): ConstraintLayout(context, attrs) {

    val titleTextView: TextView
        get() = findViewById(R.id.title_text_view)

    val valueTextView: TextView
        get() = findViewById(R.id.value_text_view)


}