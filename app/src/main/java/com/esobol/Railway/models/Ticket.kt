package com.esobol.Railway.models

import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import java.util.*

@Entity
class Ticket(val source: String,
             val destination: String,
             var departureDate: Date,
             var arrivalDate: Date,
             @PrimaryKey val id: String = UUID.randomUUID().toString()) {



}