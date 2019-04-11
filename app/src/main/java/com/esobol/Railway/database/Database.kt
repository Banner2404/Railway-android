package com.esobol.Railway.database

import android.arch.persistence.room.Database
import android.arch.persistence.room.RoomDatabase
import android.arch.persistence.room.TypeConverters
import com.esobol.Railway.models.NotificationAlert
import com.esobol.Railway.models.NotificationAlertEntity
import com.esobol.Railway.models.Place
import com.esobol.Railway.models.Ticket

@Database(entities = arrayOf(Ticket::class, Place::class, NotificationAlertEntity::class), version = 3)
@TypeConverters(DateConverter::class)
abstract class Database: RoomDatabase() {

    abstract fun ticketDao(): TicketDao
    abstract fun placeDao(): PlaceDao
    abstract fun notificationAlertDao(): NotificationAlertDao
}