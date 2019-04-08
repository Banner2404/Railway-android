package com.esobol.Railway.models

import android.arch.persistence.room.Entity
import android.arch.persistence.room.ForeignKey
import android.arch.persistence.room.PrimaryKey
import java.util.*

@Entity
class Place() {

    var carriage: Int = 0
    var seat: String = ""
    var ticketId: String = ""
    @PrimaryKey var id: String = UUID.randomUUID().toString()

}