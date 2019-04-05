package com.esobol.Railway.models

import android.arch.persistence.room.Embedded
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import android.arch.persistence.room.Relation
import java.util.*

class TicketWithPlaces {

    @Embedded
    var ticket: Ticket = Ticket("", "", Date(), Date())

    @Relation(parentColumn = "id", entityColumn = "ticketId", entity = Place::class)
    var places: List<Place> = listOf()
}

@Entity
class Ticket(val source: String,
             val destination: String,
             var departureDate: Date,
             var arrivalDate: Date,
             @PrimaryKey
             val id: String = UUID.randomUUID().toString()) {



}