package com.example.application.dto

import com.example.business.reservation.domain.Reservation

data class ReservationResult(
    val reservationId: Long,
    val userId: Long,
    val seatIds: List<Long> = listOf()
) {

    companion object {
        fun of(reservation: Reservation, seatIds: List<Long>): ReservationResult {
            return ReservationResult(
                reservationId = reservation.reservationId,
                userId = reservation.userId,
                seatIds = seatIds
            )
        }
    }

}