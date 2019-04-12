package com.esobol.Railway.activities

import android.content.DialogInterface
import android.opengl.Visibility
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.view.View
import android.widget.*
import com.esobol.Railway.MyApplication
import com.esobol.Railway.R
import com.esobol.Railway.database.TicketRepository
import com.esobol.Railway.models.NotificationAlert
import com.esobol.Railway.views.NotificationView
import javax.inject.Inject

class SettingsActivity : AppCompatActivity(), DialogInterface.OnClickListener {

    @Inject
    lateinit var ticketRepository: TicketRepository
    lateinit var linearLayout: LinearLayout
    lateinit var addNotificationButton: Button
    var notificationAlerts: ArrayList<NotificationAlert> = arrayListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)
        MyApplication.component.inject(this)

        linearLayout = findViewById(R.id.linear_layout)
        addNotificationButton = findViewById(R.id.add_notification_button)

        addNotificationButton.setOnClickListener {
            addButtonClick()
        }
        loadNotifications()
    }

    fun addButtonClick() {
        AlertDialog.Builder(this)
            .setTitle("Test")
            .setAdapter(createSpinnerAdapter(), this)
            .show()
    }

    fun loadNotifications() {
        val task = ticketRepository.getNotificationAlerts()
        task.listener = object : TicketRepository.FetchNotificationAlertsTask.Listener {
            override fun onDataLoaded(tickets: Set<NotificationAlert>) {
                notificationAlerts = ArrayList(tickets)
                showNotifications()
            }
        }
        task.execute()
    }

    fun removeNotificationViews() {
        val views = (0..linearLayout.childCount)
            .map { linearLayout.getChildAt(it) }
            .filter { it is NotificationView }

        views.forEach { linearLayout.removeView(it) }

    }

    fun showNotifications() {
        removeNotificationViews()
        notificationAlerts.indices.zip(notificationAlerts).forEach {
            addNotificationView(it.second, it.first)
        }
    }

    fun addNotification(alert: NotificationAlert) {
        notificationAlerts.add(alert)
        showNotifications()
    }

    fun addNotificationView(notificationAlert: NotificationAlert, index: Int) {
        val view = layoutInflater.inflate(R.layout.notification_view, linearLayout, false) as NotificationView
        view.titleTextView.text = titleForIndex(index)
        view.valueTextView.text = titleForNotificationAlert(notificationAlert)
        val viewIndex = linearLayout.indexOfChild(addNotificationButton)
        linearLayout.addView(view, viewIndex)

        view.setOnClickListener {
            notificationAlerts.remove(notificationAlert)
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
        return ArrayAdapter(this, android.R.layout.select_dialog_singlechoice, availableAlerts().map { titleForNotificationAlert(it) })
    }

    fun availableAlerts() : List<NotificationAlert> {
        return NotificationAlert.values().filter { !notificationAlerts.contains(it) }
    }

    override fun onClick(dialog: DialogInterface?, which: Int) {
        val alert = availableAlerts()[which]
        addNotification(alert)
    }
}
