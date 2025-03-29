package com.example.application.dto

import com.example.business.reservation.domain.Reservation
import com.example.business.seat.domain.Seat

data class ReservationResult(
    val reservationId: Long,
    val userId: Long,
    val seats: List<SeatResult> = listOf()
) {

    companion object {
        fun of(reservation: Reservation, seats: List<Seat>): ReservationResult {
            return ReservationResult(
                reservationId = reservation.reservationId,
                userId = reservation.userId,
                seats = seats.map { SeatResult.of(it) }
            )
        }
    }

}