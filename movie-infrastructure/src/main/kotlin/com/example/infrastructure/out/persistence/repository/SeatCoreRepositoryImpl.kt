package com.example.infrastructure.out.persistence.repository

import com.example.business.seat.domain.Seat
import com.example.business.seat.repository.SeatRepository
import com.example.infrastructure.out.persistence.mapper.SeatMapper
import org.springframework.stereotype.Repository

@Repository
class SeatCoreRepositoryImpl(
    private val jpaRepository: SeatJpaRepository
): SeatRepository {

    override fun getSeats(seatIds: List<Long>): List<Seat> {
        val seats = jpaRepository.findBySeatIdsWithLock(seatIds)

        return seats.map { SeatMapper.toSeat(it) }.toList()
    }

    override fun updateForReserve(seats: List<Seat>, reservationId: Long) {
        val updatedSeats = seats.map { SeatMapper.toEntity(it) }

        jpaRepository.saveAll(updatedSeats)
    }
}