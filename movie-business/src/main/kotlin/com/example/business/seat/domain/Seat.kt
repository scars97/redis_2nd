package com.example.business.seat.domain

import com.example.common.exception.BusinessException
import com.example.common.exception.ErrorCode.*

data class Seat(
    val seatId: Long,
    val seatNumber: String,
    var status: SeatStatus,
    var reservationId: Long?,
    val scheduleId: Long
) {

    fun checkAvailable() {
        if (this.status != SeatStatus.AVAILABLE || this.reservationId != null) {
            throw BusinessException(ALREADY_RESERVED, "예약된 좌석입니다 : ${this.seatId} - ${this.seatNumber}")
        }
    }

    fun reserveBy(reservationId: Long) {
        this.status = SeatStatus.RESERVED
        this.reservationId = reservationId
    }

}
