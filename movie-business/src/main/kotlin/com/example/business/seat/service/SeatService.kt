package com.example.business.seat.service

import com.example.business.seat.domain.Seat
import com.example.business.seat.repository.SeatRepository
import org.springframework.stereotype.Service

@Service
class SeatService(
    private val seatRepository: SeatRepository
) {

    fun getSeats(seatIds: List<Long>): List<Seat> {
        return seatRepository.getSeats(seatIds)
    }

    fun updateForReserve(seats: List<Seat>, reservationId: Long){
        seats.forEach { it.reserveBy(reservationId) }

        seatRepository.updateForReserve(seats, reservationId)
    }

}