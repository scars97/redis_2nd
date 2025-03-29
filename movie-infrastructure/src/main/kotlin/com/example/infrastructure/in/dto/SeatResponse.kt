package com.example.infrastructure.`in`.dto

import com.example.application.dto.SeatResult

data class SeatResponse(
    val seatId: Long,
    val seatNumber: String,
    val status: String,
    val reservationId: Long?,
    val scheduleId: Long
) {
    
    companion object {
        fun of(result: SeatResult): SeatResponse {
            return SeatResponse(
                seatId = result.seatId,
                seatNumber = result.seatNumber,
                status = result.status,
                reservationId = result.reservationId,
                scheduleId = result.scheduleId
            )
        }
    }
    
}
