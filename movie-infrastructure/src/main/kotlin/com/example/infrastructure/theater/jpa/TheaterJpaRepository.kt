package com.example.infrastructure.theater.jpa

import org.springframework.data.jpa.repository.JpaRepository

interface TheaterJpaRepository: JpaRepository<TheaterEntity, Long> {

    fun findByIdIn(theaterIds: Set<Long>): List<TheaterEntity>

}