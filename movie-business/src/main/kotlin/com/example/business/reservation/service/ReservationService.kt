package com.example.business.reservation.service

import com.example.business.reservation.domain.Reservation
import com.example.business.reservation.repository.ReservationRepository
import org.springframework.stereotype.Service

@Service
class ReservationService(
    private val reservationRepository: ReservationRepository
) {

    fun createReservation(reservation: Reservation): Reservation {
        return reservationRepository.createReservation(reservation)
    }

}