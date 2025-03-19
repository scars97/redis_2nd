package com.example.infrastructure.out.persistence.repository

import com.example.infrastructure.out.persistence.entity.TheaterEntity
import org.springframework.data.jpa.repository.JpaRepository

interface TheaterJpaRepository: JpaRepository<TheaterEntity, Long> {

    fun findByIdIn(theaterIds: Set<Long>): List<TheaterEntity>

}