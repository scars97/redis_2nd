package com.example.infrastructure.out.persistence.repository

import com.example.infrastructure.out.persistence.entity.SeatEntity
import org.springframework.data.jpa.repository.JpaRepository

interface SeatJpaRepository: JpaRepository<SeatEntity, Long> {

    fun findByIdIn(seatIds: List<Long>): List<SeatEntity>

}