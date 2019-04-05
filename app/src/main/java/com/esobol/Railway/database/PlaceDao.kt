package com.esobol.Railway.database

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.Query
import com.esobol.Railway.models.Place
import com.esobol.Railway.models.Ticket
import com.esobol.Railway.models.TicketWithPlaces

@Dao
interface PlaceDao {

    @Insert
    fun insertPlace(place: Place)
}