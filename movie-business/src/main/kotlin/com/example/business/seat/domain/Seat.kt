package com.example.business.seat.domain

import java.lang.IllegalArgumentException

data class Seat(
    val seatId: Long,
    val seatNumber: String,
    var status: SeatStatus,
    var reservationId: Long?,
    val scheduleId: Long
) {

    fun checkAvailable() {
        if (this.status != SeatStatus.AVAILABLE || this.reservationId != null) {
            throw IllegalArgumentException("예약된 좌석입니다.")
        }
    }

    fun reserveBy(reservationId: Long) {
        this.status = SeatStatus.RESERVED
        this.reservationId = reservationId
    }

}
