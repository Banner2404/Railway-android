package com.esobol.Railway.activities

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.LinearLayout
import com.esobol.Railway.MyApplication
import com.esobol.Railway.R
import com.esobol.Railway.database.TicketRepository
import com.esobol.Railway.models.NotificationAlert
import com.esobol.Railway.views.NotificationView
import javax.inject.Inject

class SettingsActivity : AppCompatActivity() {

    @Inject
    lateinit var ticketRepository: TicketRepository
    lateinit var linearLayout: LinearLayout
    lateinit var addNotificationButton: Button
    var notificationAlerts: Set<NotificationAlert> = setOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)
        MyApplication.component.inject(this)

        linearLayout = findViewById(R.id.linear_layout)
        addNotificationButton = findViewById(R.id.add_notification_button)

        addNotificationButton.setOnClickListener {
            addNotification(NotificationAlert.FIVE_MINUTES)
        }

        loadNotifications()
    }

    fun loadNotifications() {
        val task = ticketRepository.getNotificationAlerts()
        task.listener = object : TicketRepository.FetchNotificationAlertsTask.Listener {
            override fun onDataLoaded(tickets: Set<NotificationAlert>) {
                notificationAlerts = tickets
                showNotifications()
            }
        }
        task.execute()
    }

    fun showNotifications() {
        notificationAlerts.forEach {
            addNotificationView(it)
        }
    }

    fun addNotification(alert: NotificationAlert) {
        ticketRepository.addNotificationAlert(alert)
    }

    fun addNotificationView(notificationAlert: NotificationAlert) {
        val view = layoutInflater.inflate(R.layout.notification_view, linearLayout, false) as NotificationView
        view.titleTextView.text = "Test"
        view.valueTextView.text = "Value"
        val index = linearLayout.indexOfChild(addNotificationButton)
        linearLayout.addView(view, index)
    }
}
