package com.example.application.event

data class ReservationMessageEvent(
    val reservationId: Long
) {

    companion object {
        fun of(reservationId: Long): ReservationMessageEvent {
            return ReservationMessageEvent(reservationId)
        }
    }

}
