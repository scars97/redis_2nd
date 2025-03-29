package com.example.infrastructure.out.persistence.mapper

import com.example.business.reservation.domain.Reservation
import com.example.business.reservation.domain.ReservationStatus
import com.example.infrastructure.out.persistence.entity.ReservationEntity

data class ReservationMapper(
    val reservationId: Long,
    val status: ReservationStatus,
    val userId: Long
) {

    companion object {
        fun toEntity(reservation: Reservation): ReservationEntity {
            return ReservationEntity(
                status = reservation.status,
                userId = reservation.userId
            )
        }

        fun toReservation(entity: ReservationEntity): Reservation {
            return Reservation(
                reservationId = entity.id,
                status = entity.status,
                userId = entity.userId
            )
        }
    }

}
