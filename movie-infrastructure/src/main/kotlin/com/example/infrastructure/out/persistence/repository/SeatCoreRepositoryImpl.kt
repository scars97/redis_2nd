package com.example.infrastructure.out.persistence.repository

import com.example.business.seat.domain.Seat
import com.example.business.seat.repository.SeatRepository
import com.example.infrastructure.out.persistence.mapper.SeatMapper
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional

@Repository
class SeatCoreRepositoryImpl(
    private val jpaRepository: SeatJpaRepository
): SeatRepository {

    override fun getSeats(seatIds: List<Long>): List<Seat> {
        val seats = jpaRepository.findBySeatIdsWithLock(seatIds)

        return seats.map { SeatMapper.toSeat(it) }.toList()
    }

    @Transactional
    override fun updateForReserve(seatIds: List<Long>, reservationId: Long) {
        val seatEntities = jpaRepository.findAllById(seatIds)

        seatEntities.forEach { it.reserveBy(reservationId) }
    }

}