package com.example.business.seat.domain

data class Seat(
    val seatId: Long,
    val seatNumber: String,
    var status: SeatStatus,
    var reservationId: Long?,
    val scheduleId: Long,
)
