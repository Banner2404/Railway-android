package com.esobol.Railway.activities

import android.content.DialogInterface
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.view.View
import android.widget.*
import com.esobol.Railway.MyApplication
import com.esobol.Railway.R
import com.esobol.Railway.notifications.NotificationManager
import com.esobol.Railway.models.NotificationAlert
import com.esobol.Railway.views.NotificationView
import javax.inject.Inject

class SettingsActivity : AppCompatActivity(), DialogInterface.OnClickListener {

    @Inject
    lateinit var notificationManager: NotificationManager
    lateinit var linearLayout: LinearLayout
    lateinit var addNotificationButton: Button
    lateinit var notificationSwitch: Switch

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)
        MyApplication.component.inject(this)

        linearLayout = findViewById(R.id.linear_layout)
        addNotificationButton = findViewById(R.id.add_notification_button)
        notificationSwitch = findViewById(R.id.notification_switch)

        addNotificationButton.setOnClickListener {
            addButtonClick()
        }

        notificationManager.loadNotifications(object : NotificationManager.Listener {
            override fun onNotificationsLoaded() {
                showNotifications()
            }
        })

        notificationSwitch.isChecked = notificationManager.isEnabled
        notificationSwitch.setOnCheckedChangeListener { _, isChecked ->
            notificationManager.isEnabled = isChecked
        }
    }

    fun addButtonClick() {
        AlertDialog.Builder(this)
            .setTitle("Test")
            .setAdapter(createSpinnerAdapter(), this)
            .show()
    }

    fun removeNotificationViews() {
        val views = (0..linearLayout.childCount)
            .map { linearLayout.getChildAt(it) }
            .filter { it is NotificationView }

        views.forEach { linearLayout.removeView(it) }

    }

    fun showNotifications() {
        removeNotificationViews()
        notificationManager.notificationAlerts.indices.zip(notificationManager.notificationAlerts).forEach {
            addNotificationView(it.second, it.first)
        }
        updateAddNotificationButton()
    }

    fun addNotification(alert: NotificationAlert) {
        notificationManager.add(alert)
        showNotifications()
    }

    fun addNotificationView(notificationAlert: NotificationAlert, index: Int) {
        val view = layoutInflater.inflate(R.layout.notification_view, linearLayout, false) as NotificationView
        view.titleTextView.text = titleForIndex(index)
        view.valueTextView.text = titleForNotificationAlert(notificationAlert)
        val viewIndex = linearLayout.indexOfChild(addNotificationButton)
        linearLayout.addView(view, viewIndex)

        view.setOnClickListener {
            notificationManager.remove(notificationAlert)
            showNotifications()
        }
    }

    fun titleForNotificationAlert(alert: NotificationAlert) : String {
        when (alert) {
            NotificationAlert.AT_EVENT_TIME -> return getString(R.string.at_event_time)
            NotificationAlert.FIVE_MINUTES -> return getString(R.string.five_minutes)
            NotificationAlert.TEN_MINUTES -> return getString(R.string.ten_minutes)
            NotificationAlert.FIFTEEN_MINUTES -> return getString(R.string.fifteen_minutes)
            NotificationAlert.HALF_HOUR -> return getString(R.string.half_hour)
            NotificationAlert.ONE_HOUR -> return getString(R.string.one_hour)
            NotificationAlert.TWO_HOURS -> return getString(R.string.two_hours)

        }
    }

    fun titleForIndex(index: Int) : String {
        when (index + 1) {
            1 -> return getString(R.string.first)
            2 -> return getString(R.string.second)
            3 -> return getString(R.string.third)
            4 -> return getString(R.string.fourth)
            5 -> return getString(R.string.fifth)
            6 -> return getString(R.string.sixth)
            7 -> return getString(R.string.seventh)
            8 -> return getString(R.string.eighth)
            9 -> return getString(R.string.ninth)
            else -> return ""
        }
    }

    fun createSpinnerAdapter() : ListAdapter {
        val alerts = notificationManager.availableAlerts()
        return ArrayAdapter(this, android.R.layout.select_dialog_singlechoice, alerts.map { titleForNotificationAlert(it) })
    }

    fun updateAddNotificationButton() {
        if (notificationManager.availableAlerts().isEmpty()) {
            addNotificationButton.visibility = View.GONE
        } else {
            addNotificationButton.visibility = View.VISIBLE
        }
    }

    override fun onClick(dialog: DialogInterface?, which: Int) {
        val alert = notificationManager.availableAlerts()[which]
        addNotification(alert)
    }
}
