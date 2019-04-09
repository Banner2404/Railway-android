package com.esobol.Railway.database

import android.arch.persistence.room.*
import com.esobol.Railway.models.Place
import com.esobol.Railway.models.Ticket
import com.esobol.Railway.models.TicketWithPlaces

@Dao
interface PlaceDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertPlace(place: Place)

    @Delete
    fun deletePlace(place: Place)
}