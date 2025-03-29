package com.example.business.reservation.repository

import com.example.business.reservation.domain.Reservation

interface ReservationRepository {

    fun createReservation(reservation: Reservation): Reservation

}