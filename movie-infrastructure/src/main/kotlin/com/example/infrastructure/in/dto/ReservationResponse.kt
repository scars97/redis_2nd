package com.example.infrastructure.`in`.dto

import com.example.application.dto.ReservationResult

data class ReservationResponse(
    val reservationId: Long,
    val userId: Long,
    val seatIds: List<Long> = listOf()
) {

    companion object {
        fun of(result: ReservationResult): ReservationResponse {
            return ReservationResponse(
                reservationId = result.reservationId,
                userId = result.userId,
                seatIds = result.seatIds
            )
        }
    }

}
