package com.esobol.Railway.models

import android.arch.persistence.room.Entity
import android.arch.persistence.room.ForeignKey
import android.arch.persistence.room.PrimaryKey
import java.util.*

@Entity
class Place(val carriage: Int,
            val seat: String,
            val ticketId: String,
            @PrimaryKey val id: String = UUID.randomUUID().toString()) {

}