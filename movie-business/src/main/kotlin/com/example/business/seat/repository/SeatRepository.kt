package com.example.business.seat.repository

import com.example.business.seat.domain.Seat

interface SeatRepository {

    fun getSeats(seatIds: List<Long>): MutableList<Seat>

    fun updateForReserve(updateSeats: List<Seat>)

}