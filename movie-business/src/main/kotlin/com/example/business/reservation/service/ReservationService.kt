package com.example.business.reservation.service

import com.example.business.reservation.domain.Reservation
import com.example.business.reservation.repository.ReservationRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class ReservationService(
    private val reservationRepository: ReservationRepository
) {

    @Transactional
    fun createReservation(reservation: Reservation): Reservation {
        return reservationRepository.createReservation(reservation)
    }

}