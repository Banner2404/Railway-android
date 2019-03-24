package com.esobol.Railway.database

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.Query
import com.esobol.Railway.models.Ticket

@Dao
interface TicketDao {

    @Insert
    fun insertTicket(ticket: Ticket)

    @Query("SELECT * FROM Ticket ORDER BY departureDate")
    fun getAllTickets(): List<Ticket>
}