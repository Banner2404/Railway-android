package com.esobol.Railway

import android.app.Activity
import android.app.AlarmManager
import android.app.Application
import android.content.Context
import com.esobol.Railway.activities.AddTicketActivity
import com.esobol.Railway.activities.SettingsActivity
import com.esobol.Railway.activities.TicketDetailsActivity
import com.esobol.Railway.activities.TicketListActivity
import com.esobol.Railway.database.TicketRepository
import com.esobol.Railway.notifications.AlarmReceiver

class MyApplication: Application() {

    companion object {
        lateinit var context: Context
    }

    override fun onCreate() {
        super.onCreate()

        context = applicationContext
    }
}