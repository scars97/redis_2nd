package com.example.infrastructure.out.persistence.mapper

import com.example.business.seat.domain.Seat
import com.example.infrastructure.out.persistence.entity.SeatEntity

class SeatMapper {

    companion object {
        fun toSeat(entity: SeatEntity): Seat {
            return Seat(
                seatId = entity.id,
                seatNumber = entity.seatNumber,
                status = entity.status,
                reservationId = entity.reservationId,
                scheduleId = entity.scheduleId
            )
        }

        fun toEntity(seat: Seat): SeatEntity {
            return SeatEntity(
                id = seat.seatId,
                seatNumber = seat.seatNumber,
                status = seat.status,
                reservationId = seat.reservationId,
                scheduleId = seat.scheduleId
            )
        }
    }

}