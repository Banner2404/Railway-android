package com.esobol.Railway.database

import android.arch.persistence.room.Database
import android.arch.persistence.room.RoomDatabase
import android.arch.persistence.room.TypeConverters
import com.esobol.Railway.models.Place
import com.esobol.Railway.models.Ticket

@Database(entities = arrayOf(Ticket::class, Place::class), version = 2)
@TypeConverters(DateConverter::class)
abstract class Database: RoomDatabase() {

    abstract fun ticketDao(): TicketDao
    abstract fun placeDao(): PlaceDao
}