package com.example.infrastructure.out.persistence.repository

import com.example.business.seat.domain.Seat
import com.example.business.seat.repository.SeatRepository
import com.example.infrastructure.out.persistence.mapper.SeatMapper
import org.springframework.stereotype.Repository

@Repository
class SeatCoreRepositoryImpl(
    private val jpaRepository: SeatJpaRepository
): SeatRepository {

    override fun getSeats(seatIds: List<Long>): MutableList<Seat> {
        val seats = jpaRepository.findByIdIn(seatIds)

        return seats.map { SeatMapper.toSeat(it) }.toMutableList()
    }

    override fun updateForReserve(updateSeats: List<Seat>) {
        val seats = updateSeats.map { SeatMapper.toEntity(it) }.toList()

        jpaRepository.saveAll(seats)
    }
}