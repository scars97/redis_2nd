package com.example.infrastructure.out.persistence.repository

import com.example.infrastructure.out.persistence.entity.SeatEntity
import jakarta.persistence.LockModeType
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Lock
import org.springframework.data.jpa.repository.Query

interface SeatJpaRepository: JpaRepository<SeatEntity, Long> {

    @Lock(LockModeType.OPTIMISTIC)
    @Query("select s from SeatEntity s where s.id in :seatIds")
    fun findBySeatIdsWithLock(seatIds: List<Long>): List<SeatEntity>

}