package com.example.business.seat.service

import com.example.business.seat.domain.Seat
import com.example.business.seat.repository.SeatRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class SeatService(
    private val seatRepository: SeatRepository
) {

    fun getSeats(seatIds: List<Long>): MutableList<Seat> {
        return seatRepository.getSeats(seatIds)
    }

    @Transactional
    fun updateForReserve(reservationId: Long, seats: MutableList<Seat>): List<Seat> {
        seats.forEach { s ->
            s.checkAvailable()
            s.reserveBy(reservationId)
        }

        seatRepository.updateForReserve(seats)

        return seats
    }

}