package com.esobol.Railway.database

import android.arch.persistence.room.*
import com.esobol.Railway.models.Ticket
import com.esobol.Railway.models.TicketWithPlaces

@Dao
interface TicketDao {

    @Insert
    fun insertTicket(ticket: Ticket)

    @Query("SELECT * FROM Ticket ORDER BY departureDate")
    fun getAllTickets(): List<TicketWithPlaces>

    @Query("SELECT * FROM Ticket WHERE id == :id")
    fun getTicketWithId(id: String): TicketWithPlaces

    @Delete
    fun deleteTicket(ticket: Ticket)

    @Update
    fun updateTicket(ticket: Ticket)
}