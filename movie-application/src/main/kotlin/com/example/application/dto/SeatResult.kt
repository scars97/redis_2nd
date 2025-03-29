package com.example.application.dto

import com.example.business.seat.domain.Seat

data class SeatResult(
    val seatId: Long,
    val seatNumber: String,
    val status: String,
    val reservationId: Long?,
    val scheduleId: Long
) {

    companion object {
        fun of(seat: Seat): SeatResult {
            return SeatResult(
                seatId = seat.seatId,
                seatNumber = seat.seatNumber,
                status = seat.status.name,
                reservationId = seat.reservationId,
                scheduleId = seat.scheduleId,
            )
        }
    }

}