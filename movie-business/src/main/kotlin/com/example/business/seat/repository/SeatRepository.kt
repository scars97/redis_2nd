package com.example.business.seat.repository

import com.example.business.seat.domain.Seat

interface SeatRepository {

    fun getSeats(seatIds: List<Long>): List<Seat>

    fun updateForReserve(seatIds: List<Long>, reservationId: Long)

}