package com.example.infrastructure.out.persistence.repository

import com.example.business.reservation.domain.Reservation
import com.example.business.reservation.repository.ReservationRepository
import com.example.infrastructure.out.persistence.mapper.ReservationMapper
import org.springframework.stereotype.Repository

@Repository
class ReservationCoreRepositoryImpl(
    private val jpaRepository: ReservationJpaRepository
): ReservationRepository {

    override fun createReservation(reservation: Reservation): Reservation {
        val entity = jpaRepository.save(ReservationMapper.toEntity(reservation))

        return ReservationMapper.toReservation(entity)
    }
}